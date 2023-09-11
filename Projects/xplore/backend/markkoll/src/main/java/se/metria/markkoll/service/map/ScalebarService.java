package se.metria.markkoll.service.map;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Service för att generera en skalstock i en given skala.
 * Skalstocken ändrar enhet från mm till km beroende på skalan och önskad bredd.
 */
@Slf4j
@Service
@NoArgsConstructor
public class ScalebarService {
    private enum segmentMeasureUnitType {
        m, km, cm, mm
    }
    private int height = 70;
    private int numberOfSegments = 2;
    private int lrbMargin = 25; // Marginal till vänster, höger och under skalstock
    private int topMargin = 25; // Marginal ovanför skalstock
    private Color colorBorderSegment = Color.BLACK;
    private Color colorSegmentEven = Color.BLACK;
    private Color colorSegmentUneven = Color.WHITE;
    private Color colorText = Color.BLACK;
    private Font textFont = new Font("Arial", Font.PLAIN, 16);
    private segmentMeasureUnitType segmentMeasureUnit = segmentMeasureUnitType.m;
    private boolean drawScaleRatio = true;

    /**
     * Generera bild för skalstock.
     *
     * @param scale Skala för vilken bilden ska genereras.
     * @param width Initiala bredden i points. Det går ca 2.83 points per mm.
     * Den verkliga storleken kommer anpassas beroende beräkning av segment.
     * @param dpi   DPI för upplösning på bild.
     */
    public BufferedImage getImage(Double scale, double width, double dpi) {

        double extentWidth = 2.54 * scale / 100;
        double screenWidth = dpi;
        double widthInMeters = extentWidth * width / dpi;
        if (widthInMeters < 0.001) {
            // För liten skala för att kunna renderas
            return null;
        }

        double rounding = 1;
        this.segmentMeasureUnit = segmentMeasureUnitType.m;
        if (widthInMeters > 1000) {
            rounding = 500;
            this.segmentMeasureUnit = segmentMeasureUnitType.km;
        } else if (widthInMeters > 100) {
            rounding = 50;
        } else if (widthInMeters > 10) {
            rounding = 5;
        } else if (widthInMeters < 0.005) {
            rounding = 0.0005;
            this.segmentMeasureUnit = segmentMeasureUnitType.mm;
        } else if (widthInMeters < 0.05) {
            rounding = 0.005;
            this.segmentMeasureUnit = segmentMeasureUnitType.mm;
        } else if (widthInMeters < 0.5) {
            rounding = 0.05;
            this.segmentMeasureUnit = segmentMeasureUnitType.cm;
        } else if (widthInMeters < 1) {
            rounding = 0.5;
            this.segmentMeasureUnit = segmentMeasureUnitType.cm;
        }
        double segmentInMeters = Math.ceil((widthInMeters / this.numberOfSegments) / rounding) * rounding;
        double finalWidthInMeters = segmentInMeters * this.numberOfSegments;
        int scalebarDrawingWidth = (int) Math.round(finalWidthInMeters * screenWidth / extentWidth);
        int segmentDrawingWidth = scalebarDrawingWidth / this.numberOfSegments;
        int finalScalebarWidth = scalebarDrawingWidth + (this.lrbMargin * 2);

        BufferedImage bi = new BufferedImage(finalScalebarWidth, height, BufferedImage.TYPE_INT_ARGB);

        int segmentHeight = this.height - (this.lrbMargin + this.topMargin);
        Graphics2D g2D = (Graphics2D) bi.getGraphics();
        g2D.setColor(this.colorBorderSegment);
        g2D.fillRect(this.lrbMargin - 1, this.topMargin - 1,
                (this.numberOfSegments * segmentDrawingWidth) + 2,
                segmentHeight + 2);
        for (int segmentInd = 0; segmentInd < this.numberOfSegments; segmentInd++) {
            int x = this.lrbMargin + segmentInd * segmentDrawingWidth;
            int y = this.topMargin;
            if (segmentInd % 2 == 0) {
                g2D.setColor(this.colorSegmentEven);
            } else {
                g2D.setColor(this.colorSegmentUneven);
            }
            g2D.fillRect(x, y, segmentDrawingWidth, segmentHeight);
            this.drawSegmentText(g2D,
                    segmentInd * segmentInMeters,
                    this.lrbMargin + segmentInd * segmentDrawingWidth,
                    this.height);
        }
        this.drawSegmentText(g2D,
                this.numberOfSegments * segmentInMeters,
                this.lrbMargin + this.numberOfSegments * segmentDrawingWidth,
                this.height);

        if (scale != null && drawScaleRatio) {
            String scaleText;
            DecimalFormat df = new DecimalFormat("#,###,###");
            df.setRoundingMode(RoundingMode.HALF_UP);
            if (scale < 1) {
                df = new DecimalFormat("0.##");
            }
            scaleText = df.format(scale);
            scaleText = String.format("1 : %s", scaleText);
            this.drawText(g2D, scaleText, finalScalebarWidth / 2, this.topMargin - 8);
        }

        return bi;
    }

    private void drawText(Graphics2D g2D, String txt, int x, int y) {
        g2D.setFont(this.textFont);
        FontMetrics fontMetrics = g2D.getFontMetrics();
        int txtWidth = fontMetrics.stringWidth(txt);
        x = x - txtWidth / 2;
        g2D.setColor(this.colorText);
        g2D.drawString(txt, x, y);
    }

    private void drawSegmentText(Graphics2D g2D, double segmentMeasureInMeters, int x, int y) {
        String measureToWrite = "";
        if (this.segmentMeasureUnit == segmentMeasureUnitType.km) {
            DecimalFormat decFormat;
            if (segmentMeasureInMeters / 1000 < 10) {
                decFormat = new DecimalFormat("#.#");
            } else {
                decFormat = new DecimalFormat("#");
            }
            measureToWrite = decFormat.format(((double) segmentMeasureInMeters / 1000));
        } else if (this.segmentMeasureUnit == segmentMeasureUnitType.cm) {
            DecimalFormat decFormat = new DecimalFormat("#");
            measureToWrite = decFormat.format(((double) segmentMeasureInMeters * 100));
        } else if (this.segmentMeasureUnit == segmentMeasureUnitType.mm) {
            DecimalFormat decFormat;
            if (segmentMeasureInMeters * 1000 < 1) {
                decFormat = new DecimalFormat("#.#");
            } else {
                decFormat = new DecimalFormat("#");
            }
            measureToWrite = decFormat.format(((double) segmentMeasureInMeters * 1000));
        } else {
            DecimalFormat decFormat = new DecimalFormat("#");
            measureToWrite = decFormat.format(segmentMeasureInMeters);
        }
        measureToWrite = String.format("%s %s", measureToWrite, this.segmentMeasureUnit);
        this.drawText(g2D, measureToWrite, x, y);
    }
}

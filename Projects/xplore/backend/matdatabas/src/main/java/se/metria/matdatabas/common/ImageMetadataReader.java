package se.metria.matdatabas.common;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

import java.io.*;

public class ImageMetadataReader {
    private ImageInfo imageInfo;
    private ImageMetadata metadata;

    public ImageMetadataReader(String imagePath) throws IOException, ImageReadException {
        File file = new File(imagePath);
        this.imageInfo = Imaging.getImageInfo(file);
        this.metadata = Imaging.getMetadata(file);
    }

    public ImageMetadataReader(InputStream input) throws IOException, ImageReadException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        input.transferTo(os);

        var imageInfoStream = new ByteArrayInputStream(os.toByteArray());
        var metadataStream = new ByteArrayInputStream(os.toByteArray());

        this.imageInfo = Imaging.getImageInfo(imageInfoStream, null);
        this.metadata = Imaging.getMetadata(metadataStream.readAllBytes());
    }

    public Integer getImageWidth() {
        return imageInfo.getWidth();
    }

    public Integer getImageHeight() {
        return imageInfo.getHeight();
    }

    public Integer getOrientation() {
        return getTagValueInteger(TiffTagConstants.TIFF_TAG_ORIENTATION);
    }

    private boolean tagValueExists(final TagInfo tagInfo) {
        var value = getTagValue(tagInfo);
        return value != null;
    }

    private Integer getTagValueInteger(final TagInfo tagInfo) {
        if (tagValueExists(tagInfo)) {
            return Integer.valueOf(getTagValue(tagInfo));
        } else {
            return 0;
        }
    }

    private String getTagValue(final TagInfo tagInfo) {
        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
            return field.getValueDescription();
        } else {
            return null;
        }
    }
}

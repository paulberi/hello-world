package se.metria.matdatabas.service.bifogadfil;

import org.apache.commons.imaging.ImageReadException;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.common.ImageMetadataReader;
import se.metria.matdatabas.service.bifogadfil.dto.Bifogadfil;
import se.metria.matdatabas.service.bifogadfil.dto.BifogadfilInfo;
import se.metria.matdatabas.service.bifogadfil.dto.SaveBifogadfil;
import se.metria.matdatabas.service.bifogadfil.entity.BifogadfilEntity;
import se.metria.matdatabas.service.bifogadfil.exception.BifogadfilNotFoundException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.awt.Image.SCALE_SMOOTH;
import static java.util.stream.Collectors.toSet;

@Service
public class BifogadfilService {

	private BifogadfilRepository repository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public BifogadfilService(BifogadfilRepository repository) {
		this.repository = repository;
	}
	
	public Optional<Bifogadfil> getBifogadfil(UUID id) {
		return repository.findById(id)
				.map(Bifogadfil::fromEntity);
	}

	public Optional<BifogadfilInfo> getBifogadfilInfo(UUID id) {
		return repository.findBifogadfilInfoById(id).map(BifogadfilInfo::fromView);
	}

	public Set<BifogadfilInfo> getBifogadfilInfos(Set<UUID> ids) {
		return repository.findAllBifogadfilInfoByIdIn(ids).stream()
				.map(BifogadfilInfo::fromView)
				.collect(toSet());
	}

    public Bifogadfil createBifogadfil(SaveBifogadfil saveBifogadfil) throws IOException {
        return Bifogadfil.fromEntity(persist(saveBifogadfil.toEntity()));
    }

	@Transactional
	public void deleteBifogadefiler(List<UUID> ids) throws BifogadfilNotFoundException  {
		for (UUID id : ids) {
			ensureExists(id);
		}

		repository.removeByIdIn(ids);
	}

	public void updateThumbnail(UUID id) throws IOException  {
		var entity = repository.findById(id).get();
		createThumbnailUsingExifOrientation(entity);
		repository.saveAndFlush(entity);
	}

	private void ensureExists(UUID id) throws BifogadfilNotFoundException {
		repository.findById(id).orElseThrow(BifogadfilNotFoundException::new);
	}

	private BifogadfilEntity persist(BifogadfilEntity entity) throws IOException  {
		createThumbnailUsingExifOrientation(entity);
		return repository.saveAndFlush(entity);
	}

    private void createThumbnail(BifogadfilEntity entity) throws IOException {
        if (needsThumbnail(entity)) {
            try (var in = new ByteArrayInputStream(entity.getFil()); var out = new ByteArrayOutputStream()) {
                var image = ImageIO.read(in);
                var format = getFormat(entity);

                var width = image.getWidth();
                var height = image.getHeight();
                var scaleFactor = Math.max(Math.max(width, height) / 200d, 1d);
                var newWidth = (int) Math.round(width / scaleFactor);
                var newHeight = (int) Math.round(height / scaleFactor);

                var thumbnail = scaleImage(image, newWidth, newHeight);
                ImageIO.write(thumbnail, format, out);
                entity.setThumbnail(out.toByteArray());
            }
        }
    }

    private void createThumbnailUsingExifOrientation(BifogadfilEntity entity) throws IOException {
        if (needsThumbnail(entity)) {
            try (var in = new ByteArrayInputStream(entity.getFil()); var out = new ByteArrayOutputStream()) {
                in.mark(0);

                var image = ImageIO.read(in);
                var format = getFormat(entity);

                var width = image.getWidth();
                var height = image.getHeight();
                var scaleFactor = Math.max(Math.max(width, height) / 200d, 1d);
                var newWidth = (int) Math.round(width / scaleFactor);
                var newHeight = (int) Math.round(height / scaleFactor);
                var orientation = 0;

                in.reset();

                try {
                    var reader = new ImageMetadataReader(in);
                    orientation = reader.getOrientation();
                } catch (ImageReadException e) {
                    logger.error("Failed to read EXIF information. Using default orientation.");
                }

                // Resize and rotate image based on EXIF orientation.
                var thumbnail = scaleImage(image, newWidth, newHeight);
                thumbnail = rotateImage(thumbnail, orientation);

                ImageIO.write(thumbnail, format, out);
                entity.setThumbnail(out.toByteArray());
            }
        }
    }

	private boolean needsThumbnail(BifogadfilEntity entity) {
		return entity.getFil() != null && entity.getThumbnail() == null && isImage(entity);
	}
	
	private String getFormat(BifogadfilEntity entity) {
		return entity.getMimeTyp() != null ? entity.getMimeTyp().substring(entity.getMimeTyp().indexOf("/") + 1) : "";
	}

	private boolean isImage(BifogadfilEntity entity) {
		return entity.getMimeTyp() != null && entity.getMimeTyp().startsWith("image/");
	}

    private BufferedImage scaleImage(BufferedImage image, int newWidth, int newHeight) {
        Graphics2D g = null;
        try {
            var thumbnail = new BufferedImage(newWidth, newHeight, image.getType());
            g = thumbnail.createGraphics();
            g.drawImage(image.getScaledInstance(newWidth, newHeight, SCALE_SMOOTH), 0, 0, null);
            return thumbnail;
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
    }

    private BufferedImage rotateImage(BufferedImage image, int orientation) {
        AffineTransform t = getTransform(image.getWidth(), image.getHeight(), orientation);
        Dimension rotatedSize = getRotatedSize(image.getWidth(), image.getHeight(), orientation);

        AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BICUBIC);
        BufferedImage rotatedImage = new BufferedImage(rotatedSize.width, rotatedSize.height, image.getType());
        op.filter(image, rotatedImage);

        return rotatedImage;
    }

    private Dimension getRotatedSize(int width, int height, int orientation) {
        if (orientation > 4 && orientation <= 8) {
            return new Dimension(height, width);
        } else {
            return new Dimension(width, height);
        }
    }

    private AffineTransform getTransform(int width, int height, int orientation) {
        AffineTransform t = new AffineTransform();
        switch (orientation) {
            case 1:
                // 0 degrees rotation. No transform necessary.
                break;
            case 2: // 0 degrees rotation, mirrored
                t.scale(-1.0, 1.0);
                t.translate(-width, 0);
                break;
            case 3: // 180 degrees rotation
                t.quadrantRotate(2, width / 2, height / 2);
                break;
            case 4: // 180 degrees rotation and mirrored
                t.scale(1.0, -1.0);
                t.translate(0, -height);
                break;
            case 5: // 90 degrees rotation and mirrored
                t.quadrantRotate(3);
                t.scale(-1.0, 1.0);
                break;
            case 6: // 90 degrees rotation
                t.translate(height, 0);
                t.quadrantRotate(1);
                break;
            case 7: // 270 degrees rotation and mirrored
                t.translate(height, 0);
                t.scale(-1.0, 1.0);
                t.translate(0, width);
                t.quadrantRotate(3);
                break;
            case 8: // 270 degrees rotation
                t.translate(0, width);
                t.quadrantRotate(3);
                break;
        }
        return t;
    }
}

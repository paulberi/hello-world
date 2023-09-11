package se.metria.matdatabas.restapi.bifogadfil;

import org.modelmapper.Converter;

import java.util.UUID;

public class BifogadFilHelper {
	
	public static Converter<UUID, String> toFilLink = ctx -> BifogadFilHelper.getFilLink(ctx.getSource());
	public static Converter<UUID, String> toThumbnailLink = ctx -> BifogadFilHelper.getThumbnailLink(ctx.getSource());

	private BifogadFilHelper() {
	}

	public static String getFilLink(UUID id) {
		return id != null ? "/api/bifogadfil/" + id.toString() + "/data" : null;
	}
	
	public static String getThumbnailLink(UUID id) {
		return id != null ? "/api/bifogadfil/" + id.toString() + "/thumbnail" : null;
	}
}

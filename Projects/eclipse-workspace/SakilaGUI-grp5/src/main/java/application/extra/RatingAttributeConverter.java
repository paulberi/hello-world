package application.extra;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RatingAttributeConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating attribute) {
        if (attribute == null)
            return null;

        switch (attribute) {
        case G:
            return "G";

        case PG:
            return "PG";

        case PG_13:
            return "PG-13";

        case R:
            return "R";

        case NC_17:
            return "NC-17";

        default:
            throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public Rating convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;

        switch (dbData) {
        case "G":
            return Rating.G;

        case "PG":
            return Rating.PG;

        case "PG-13":
            return Rating.PG_13;

        case "R":
            return Rating.R;

        case "NC-17":
            return Rating.NC_17;

        default:
            throw new IllegalArgumentException(dbData + " not supported.");
        }
    }

}

package se.metria.markkoll.util;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import se.metria.markkoll.entity.markagare.MarkagareEntity;

import java.text.MessageFormat;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@NoArgsConstructor
public class FodelsedatumEllerOrganisationsnummerConverter implements Converter<MarkagareEntity, String> {
    @Override
    public String convert(MappingContext<MarkagareEntity, String> mappingContext) {
        var personnummer = mappingContext.getSource().getPerson().getPersonnummer();
        if (StringUtils.isEmpty(personnummer)) {
            return "";
        }
        else if (isOrganisationsnummer(personnummer)) {
            return personnummer;
        }
        else {
            return personnummer.substring(0, Math.max(0, personnummer.length() - 5));
        }
    }

    private Boolean isOrganisationsnummer(String personnummer) {
        var onlyNumbers = personnummer.replaceAll("\\D+","");
        if (isEmpty(onlyNumbers)) {
            return null;
        }

        /*
        Saxat från Wikipedia, denna kunskapens outsinliga källa:

        För att skilja på personnummer och organisationsnummer är alltid "månaden" (andra paret, som byggs upp av
        tredje och fjärde siffran) i ett organisationsnummer minst 20.
        */
        int monthValue;
        try {
            if (onlyNumbers.length() == 6 || onlyNumbers.length() == 8) {
                return true;
            }
            else if (onlyNumbers.length() == 10) {
                monthValue = Integer.parseInt(onlyNumbers.substring(2, 4));
            }
            else if (onlyNumbers.length() == 12) {
                monthValue = Integer.parseInt(onlyNumbers.substring(4, 6));
            }
            else {
                throw new IllegalArgumentException(MessageFormat.format("Ogiltigt personnummer: {0}", onlyNumbers));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                MessageFormat.format("Kunde inte formatera \"månadsdelen\" av personnummret {0} till ett heltal",
                    onlyNumbers)
            );
        }

        return monthValue >= 20;
    }
}

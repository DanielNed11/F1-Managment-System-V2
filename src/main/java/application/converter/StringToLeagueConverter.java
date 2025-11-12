package application.converter;

import application.domain.League;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLeagueConverter implements Converter<String, League> {

    @Override
    public League convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }

        try {
            return League.valueOf(source);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

package se.atg.harrykart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.atg.harrykart.dto.Ranking;
import se.atg.harrykart.dto.Response;
import se.atg.harrykart.schemas.HarryKartType;
import se.atg.harrykart.utils.HarryKartRequestUtil;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class HarryKartServiceImpl implements HarryKartService {

    private Boolean schemaValidation;
    private Integer trackLength;
    private Integer numOfPositions;


    public HarryKartServiceImpl( @Value("${schema.validation.enabled}") Boolean schemaValidation,
                                 @Value("${track.length}") Integer trackLength,
                                 @Value("${number.of.positions}") Integer numOfPositions) {
        this.schemaValidation = schemaValidation;
        this.trackLength = trackLength;
        this.numOfPositions =  numOfPositions;

    }

    @Override
    public Response calculateRanking(String request) throws Exception {
        HarryKartType harryKartType = (HarryKartType) HarryKartRequestUtil.instance().xmlToObject(request, schemaValidation);

        AtomicInteger position = new AtomicInteger(0);

        List<Ranking> rankings = harryKartType.getStartList().getParticipant().stream().map(participant -> {
            Ranking ranking = new Ranking(0, participant.getName(), trackLength / participant.getBaseSpeed().doubleValue());
            harryKartType.getPowerUps().getLoop().stream()
                    .forEach(loop -> loop.getLane().stream()
                            .filter(lane -> lane.getNumber().intValue() == participant.getLane().intValue()).
                                    forEach(lane -> {
                                        participant.setBaseSpeed(BigInteger.valueOf(participant.getBaseSpeed().intValue() + lane.getValue().intValue()));
                                        ranking.setTimeTaken(ranking.getTimeTaken() + (trackLength / participant.getBaseSpeed().intValue()));
                                    }));
            return ranking;
        }).sorted(Comparator.comparing(Ranking::getTimeTaken))
                .map(ranking -> {
                    ranking.setPosition(position.incrementAndGet());
                    return ranking;
                }).filter(ranking -> ranking.getPosition() <= numOfPositions).
                        collect(Collectors.toList());

        return Response.builder().ranking(rankings).build();
    }
}

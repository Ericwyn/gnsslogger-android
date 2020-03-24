package com.example.gnssloggerbtf.RinexFilefragments.Constellations;

import android.location.GnssClock;
import android.location.GnssMeasurementsEvent;


import com.example.gnssloggerbtf.RinexFilefragments.GNSSConstants;

import static java.lang.Math.max;

public class GalileoIonoFreeConstellation extends GalileoConstellation {

    private GalileoConstellation galileoConstellation = new GalileoConstellation();
    private GalileoE5aConstellation galileoE5aConstellation = new GalileoE5aConstellation();
//    GalileoE1Constellation galileoIonoFreeConstellation = new GalileoE1Constellation();

    private static final String NAME = "Galileo IF";

//    private Time timeRefMsec;

    @Override
    public void updateMeasurements(GnssMeasurementsEvent event) {
        synchronized (this) {

//            timeRefMsec = new Time(System.currentTimeMillis());

            galileoConstellation.updateMeasurements(event);
            galileoE5aConstellation.updateMeasurements(event);
            GnssClock gnssClock=event.getClock();

            observedSatellites.clear();

            for(SatelliteParameters satelliteE1 : galileoConstellation.getSatellites()) {

                SatelliteParameters satelliteE5a = null;

                for(SatelliteParameters satelliteParameters: galileoE5aConstellation.getSatellites()){
                    if(satelliteParameters.getSatId() == satelliteE1.getSatId()) {
                        satelliteE5a = satelliteParameters;
                        break;
                    }
                }

                if(satelliteE5a != null) {
                    // Get the code based pseudoranges on the E1 and E5a frequencies
                    double pseudorangeE1    =  satelliteE1.getPseudorange();
                    double pseudorangeE5a   =  satelliteE5a.getPseudorange();

                    // Form the ionosphere-free (IF) combination
                    double pseudorangeIF    = (Math.pow(GNSSConstants.FE1,2) * pseudorangeE1 - Math.pow(GNSSConstants.FE5a,2) * pseudorangeE5a)/(Math.pow(GNSSConstants.FE1,2)- Math.pow(GNSSConstants.FE5a,2));

                    SatelliteParameters newSatellite = new SatelliteParameters(new GpsTime(gnssClock),
                            satelliteE1.getSatId(),
                            new Pseudorange(pseudorangeIF, 0.0));

//                    newSatellite.setUniqueSatId("E"+satelliteE1.getSatId()+"<sub>IF</sub>");
                    newSatellite.setUniqueSatId("E"+satelliteE1.getSatId()+"_IF");

                    //todo: assign properly
                    newSatellite.setSignalStrength(
                            (satelliteE1.getSignalStrength() + satelliteE5a.getSignalStrength())/2);

                    newSatellite.setConstellationType(satelliteE1.getConstellationType());

                    observedSatellites.add(newSatellite);
                }
            }

            visibleButNotUsed = max(galileoConstellation.getVisibleConstellationSize(), galileoE5aConstellation.getVisibleConstellationSize())-observedSatellites.size();

            tRxGalileoTOW = galileoConstellation.gettRxGalileoTOW();

            weekNumber = galileoConstellation.getWeekNumber()*GNSSConstants.NUMBER_NANO_SECONDS_PER_WEEK;

        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getConstellationId() {
        synchronized (this) {
            return Constellation.CONSTELLATION_GALILEO_IonoFree;
        }
    }

    public static void registerClass() {
        register(
                NAME,
                GalileoIonoFreeConstellation.class);
    }
}

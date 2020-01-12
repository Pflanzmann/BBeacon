package com.bbeacon.backend;

import com.bbeacon.exceptions.DataSetDoesNotFitException;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.RawDataSet;
import com.bbeacon.models.UncalibratedBeacon;

/**
 * Temporarly stores the data from a calibration to evalute it to return a calibrated beacon out of a uncalibrated beacon
 */
public interface EvaluatorType {

    /**
     * temporary stores the calibration-data and evaluates those
     *
     * @param dataSet the measured DataSet that should be added
     * @throws DataSetDoesNotFitException
     */
    void insertRawDataSet(RawDataSet<Integer> dataSet) throws DataSetDoesNotFitException;

    /**
     * takes the evaluated data and returns them as a calibrated beacon
     *
     * @param uncalibratedBeacon takes a uncalibrated beacon to set the basis for the return value
     * @return returned a finished calibrated beacon
     */
    CalibratedBeacon evaluateAndFinish(UncalibratedBeacon uncalibratedBeacon);
}

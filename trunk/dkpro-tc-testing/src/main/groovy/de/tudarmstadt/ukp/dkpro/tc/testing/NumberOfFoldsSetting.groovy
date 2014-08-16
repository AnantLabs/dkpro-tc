/**
 * Copyright 2014
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package de.tudarmstadt.ukp.dkpro.tc.testing;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.tc.core.Constants;
import de.tudarmstadt.ukp.dkpro.tc.weka.task.BatchTaskCrossValidation
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription

import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.resource.ResourceInitializationException

import weka.classifiers.bayes.NaiveBayes
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter
import de.tudarmstadt.ukp.dkpro.lab.Lab
import de.tudarmstadt.ukp.dkpro.lab.task.Dimension
import de.tudarmstadt.ukp.dkpro.lab.task.impl.BatchTask.ExecutionPolicy
import de.tudarmstadt.ukp.dkpro.tc.core.Constants
import de.tudarmstadt.ukp.dkpro.tc.features.length.NrOfTokensDFE
import de.tudarmstadt.ukp.dkpro.tc.testing.io.LineInstanceReader
import de.tudarmstadt.ukp.dkpro.tc.weka.report.WekaBatchCrossValidationReport
import de.tudarmstadt.ukp.dkpro.tc.weka.report.WekaClassificationReport
import de.tudarmstadt.ukp.dkpro.tc.weka.task.BatchTaskCrossValidation
import de.tudarmstadt.ukp.dkpro.tc.weka.writer.WekaDataWriter

/**
 * Testing the check in the CV batch task that the number of folds needs to be at least 2.
 *
 */
public class NumberOfFoldsSetting implements Constants {

    def experimentName = "BatchCvFoldTest";

    // === PARAMETERS===========================================================

    def corpusFilePathTrain = "classpath:/data/smalltexts/smallInstances.txt.gz";
    def languageCode = "en";
    def numFolds = 1;

    // === DIMENSIONS===========================================================

    def dimReaders = Dimension.createBundle("readers", [
        readerTrain: LineInstanceReader.class,
        readerTrainParams: [
            LineInstanceReader.PARAM_SOURCE_LOCATION,
            corpusFilePathTrain]
    ]);

    def dimLearningMode = Dimension.create(DIM_LEARNING_MODE, LM_SINGLE_LABEL);
    def dimFeatureMode = Dimension.create(DIM_FEATURE_MODE, FM_DOCUMENT);
    def dimDataWriter = Dimension.create(DIM_DATA_WRITER, WekaDataWriter.class.name);


    def dimClassificationArgs = Dimension.create(
    DIM_CLASSIFICATION_ARGS,
    [NaiveBayes.class.name]);

    def dimFeatureSets = Dimension.create(
    DIM_FEATURE_SET,
    [
        NrOfTokensDFE.class.name
    ]);

    // === Test =========================================================

    public void run() throws Exception
    {

        BatchTaskCrossValidation batchTask = [
            experimentName: experimentName + "-CV-Groovy",
            type: "Evaluation-"+ experimentName +"-CV-Groovy",
            preprocessingPipeline: getPreprocessing(),
            innerReports: [WekaClassificationReport.class],
            parameterSpace : [
                dimReaders,
                dimFeatureMode,
                dimLearningMode,
                dimDataWriter,
                dimClassificationArgs,
                dimFeatureSets
            ],
            executionPolicy: ExecutionPolicy.RUN_AGAIN,
            reports:         [WekaBatchCrossValidationReport],
            numFolds: numFolds];

        Lab.getInstance().run(batchTask);
    }

    private AnalysisEngineDescription getPreprocessing()
    throws ResourceInitializationException
    {
        return createEngineDescription(
        createEngineDescription(BreakIteratorSegmenter.class)
        );
    }

    public static void main(String[] args)
    {
        new NumberOfFoldsSetting().run();
    }
}
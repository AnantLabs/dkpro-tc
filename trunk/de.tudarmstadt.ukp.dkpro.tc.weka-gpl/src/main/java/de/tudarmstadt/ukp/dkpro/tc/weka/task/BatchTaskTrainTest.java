package de.tudarmstadt.ukp.dkpro.tc.weka.task;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;

import de.tudarmstadt.ukp.dkpro.lab.engine.TaskContext;
import de.tudarmstadt.ukp.dkpro.lab.reporting.Report;
import de.tudarmstadt.ukp.dkpro.lab.task.impl.BatchTask;
import de.tudarmstadt.ukp.dkpro.tc.core.task.ExtractFeaturesTask;
import de.tudarmstadt.ukp.dkpro.tc.core.task.MetaInfoTask;
import de.tudarmstadt.ukp.dkpro.tc.core.task.PreprocessTask;
import de.tudarmstadt.ukp.dkpro.tc.weka.report.FeatureValuesReport;
import de.tudarmstadt.ukp.dkpro.tc.weka.report.OutcomeIDReport;

/**
 * Train-Test setup
 * 
 * @author daxenberger
 * 
 */
public class BatchTaskTrainTest
    extends BatchTask
{

    private String experimentName;
    private CollectionReaderDescription readerTrain;
    private CollectionReaderDescription readerTest;
    private AnalysisEngineDescription aggregate;
    private String dataWriter;
    private Class<? extends Report> innerReport;

    private PreprocessTask preprocessTaskTrain;
    private PreprocessTask preprocessTaskTest;
    private MetaInfoTask metaTask;
    private ExtractFeaturesTask featuresTrainTask;
    private ExtractFeaturesTask featuresTestTask;
    private TestTask testTask;

    public BatchTaskTrainTest()
    {/* needed for Groovy */
    }

    /**
     * Preconfigured train-test setup which should work out-of-the-box. You might want to set a
     * report to collect the results.
     * 
     * @param aExperimentName
     *            name of the experiment
     * @param aReaderTrain
     *            collection reader for train data
     * @param aReaderTest
     *            collection reader for test data
     * @param aAggregate
     *            preprocessing analysis engine aggregate
     * @param aDataWriterClassName
     *            data writer class name
     */
    public BatchTaskTrainTest(String aExperimentName, CollectionReaderDescription aReaderTrain,
            CollectionReaderDescription aReaderTest, AnalysisEngineDescription aAggregate,
            String aDataWriterClassName)
    {
        setExperimentName(aExperimentName);
        setReaderTrain(aReaderTrain);
        setReaderTest(aReaderTest);
        setAggregate(aAggregate);
        setDataWriter(aDataWriterClassName);
    }

    @Override
    public void execute(TaskContext aContext)
        throws Exception
    {
        init();
        super.execute(aContext);
    }

    /**
     * Initializes the experiment. This is called automatically before execution. It's not done
     * directly in the constructor, because we want to be able to use setters instead of the
     * three-argument constructor.
     * 
     * @throws IllegalStateException
     *             if not all necessary arguments have been set.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void init()
    {
        if (experimentName == null || readerTrain == null || readerTest == null
                || aggregate == null || dataWriter == null)

        {
            throw new IllegalStateException(
                    "You must set Experiment Name, Test Reader, Training Reader, DataWriter and Aggregate.");
        }

        // preprocessing on training data
        preprocessTaskTrain = new PreprocessTask();
        preprocessTaskTrain.setReader(readerTrain);
        preprocessTaskTrain.setAggregate(aggregate);
        preprocessTaskTrain.setTesting(false);
        preprocessTaskTrain.setType(preprocessTaskTrain.getType() + "-Train-" + experimentName);

        // preprocessing on test data
        preprocessTaskTest = new PreprocessTask();
        preprocessTaskTest.setReader(readerTest);
        preprocessTaskTest.setAggregate(aggregate);
        preprocessTaskTest.setTesting(true);
        preprocessTaskTest.setType(preprocessTaskTest.getType() + "-Test-" + experimentName);

        // get some meta data depending on the whole document collection that we need for training
        metaTask = new MetaInfoTask();
        metaTask.setType(metaTask.getType() + "-" + experimentName);
        metaTask.addImportLatest(MetaInfoTask.INPUT_KEY, PreprocessTask.OUTPUT_KEY_TRAIN,
                preprocessTaskTrain.getType());

        // feature extraction on training data
        featuresTrainTask = new ExtractFeaturesTask();
        featuresTrainTask.setAddInstanceId(true);
        featuresTrainTask.setDataWriter(dataWriter);
        featuresTrainTask.setType(featuresTrainTask.getType() + "-Train-" + experimentName);
        featuresTrainTask.addImportLatest(MetaInfoTask.META_KEY, MetaInfoTask.META_KEY,
                metaTask.getType());
        featuresTrainTask.addImportLatest(ExtractFeaturesTask.INPUT_KEY,
                PreprocessTask.OUTPUT_KEY_TRAIN,
                preprocessTaskTrain.getType());

        // feature extraction on test data
        featuresTestTask = new ExtractFeaturesTask();
        featuresTestTask.setAddInstanceId(true);
        featuresTestTask.setDataWriter(dataWriter);
        featuresTestTask.setType(featuresTestTask.getType() + "-Test-" + experimentName);
        featuresTestTask.addImportLatest(MetaInfoTask.META_KEY, MetaInfoTask.META_KEY,
                metaTask.getType());
        featuresTestTask.addImportLatest(ExtractFeaturesTask.INPUT_KEY,
                PreprocessTask.OUTPUT_KEY_TEST,
                preprocessTaskTest.getType());

        // test task operating on the models of the feature extraction train and test tasks
        testTask = new TestTask();
        testTask.setType(testTask.getType() + "-" + experimentName);
        testTask.addReport(FeatureValuesReport.class);
        if (innerReport != null) {
            testTask.addReport(innerReport);
        }
        testTask.addReport(OutcomeIDReport.class);
        testTask.addImportLatest(TestTask.INPUT_KEY_TRAIN, ExtractFeaturesTask.OUTPUT_KEY,
                featuresTrainTask.getType());
        testTask.addImportLatest(TestTask.INPUT_KEY_TEST, ExtractFeaturesTask.OUTPUT_KEY,
                featuresTestTask.getType());

        addTask(preprocessTaskTrain);
        addTask(preprocessTaskTest);
        addTask(metaTask);
        addTask(featuresTrainTask);
        addTask(featuresTestTask);
        addTask(testTask);
    }

    public void setExperimentName(String experimentName)
    {
        this.experimentName = experimentName;
    }

    public void setReaderTest(CollectionReaderDescription aReader)
    {
        this.readerTest = aReader;
    }

    public void setReaderTrain(CollectionReaderDescription aReader)
    {
        this.readerTrain = aReader;
    }

    public void setAggregate(AnalysisEngineDescription aggregate)
    {
        this.aggregate = aggregate;
    }

    public String getDataWriter()
    {
        return dataWriter;
    }

    public void setDataWriter(String dataWriter)
    {
        this.dataWriter = dataWriter;
    }

    /**
     * Sets the report for the test task
     * 
     * @param innerReport
     *            classification report or regression report
     */
    public void setInnerReport(Class<? extends Report> innerReport)
    {
        this.innerReport = innerReport;
    }
}
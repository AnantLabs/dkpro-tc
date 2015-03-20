This tutorial explains how to use JSON instead of the (recommended) Groovy classes to configure your experiments.

**Please note: The configuration code in this tutorial is incomplete and not necessarily up-to-date with the latest release of DKPro-TC. It is intended as a starting point for users who want to create their own experiment configuration using JSON, but it will not work as is.**

# Example JSON file #

This is an example JSON file specifying a couple of parameters for an experiment.

```
{
    "corpusFilePathTrain" : "src/main/resources/data/bydate-train",
    "nGramMinSize" : 1,
    "nGramMaxSize" : 3,
    "pipelineParameters" :
    [
       {"pipelineParameter" :
          ["TopK", "500"]
       },
       {"pipelineParameter" :
          ["TopK", "1000"]
       }
    ]
}       
```

# Example Java classes building upon a JSON configuration #

## ParameterSpaceParser ##

This ParameterSpaceParser class parses the JSON file and passes all necessary configuration into the ParameterSpace.

```
public class ParameterSpaceParser
{
    public static ParameterSpace createParamSpaceFromJson(JSONObject pipelineConfiguration)
        throws IOException

    {

        final String corpusFilePathTrain = pipelineConfiguration.getString("corpusFilePathTrain");

        // DIMENSIONS for pipelineParameters
        Object[] specialPipelineParameters = new Object[] {
                NGramFeatureExtractor.PARAM_NGRAM_MIN_N,
                pipelineConfiguration.getInt("nGramMinSize"),
                NGramFeatureExtractor.PARAM_NGRAM_MAX_N,
                pipelineConfiguration.getInt("nGramMaxSize") };
        JSONArray pipelineParamsArg0 = pipelineConfiguration.getJSONArray("pipelineParameters");
        List<List<Object>> pipelineParameters = new ArrayList<List<Object>>();
        for (Object object : pipelineParamsArg0) {
            JSONObject jObj = (JSONObject) object;
            Object[] array = jObj.getJSONArray("pipelineParameter").toArray(new String[0]);
            List<Object> args = new ArrayList<Object>(Arrays.asList(array));
            for (Object specialArg : specialPipelineParameters) {
                args.add(specialArg);
            }
            pipelineParameters.add(args);
        }

        // DIMENSIONS for readers
        Map<String, Object> dimReaderTrain = new HashMap<String, Object>();
        dimReaderTrain.put("readerTrain", TwentyNewsgroupsCorpusReader.class);
        dimReaderTrain.put("readerTrainParams", Arrays.asList(
                TwentyNewsgroupsCorpusReader.PARAM_SOURCE_LOCATION, corpusFilePathTrain,
                TwentyNewsgroupsCorpusReader.PARAM_PATTERNS,
                TwentyNewsgroupsCorpusReader.INCLUDE_PREFIX + "*/*.txt"
                ));

        // other parameters
        // ...

        ParameterSpace pSpace = new ParameterSpace(
                Dimension.createBundle("readerTrain", dimReaderTrain),
                Dimension.create("pipelineParameters", pipelineParameters.toArray()),
                // other dimensions...
        return pSpace;
    }
}
```

## Java main class ##

This Java class will actually run the experiment with the ParameterSpace as configured.

```
public class TwentyNewsgroupsExperiment
{

    private String languageCode;

    public static void main(String[] args)
        throws Exception
    {
        TwentyNewsgroupsExperiment experiment = new TwentyNewsgroupsExperiment();
        ParameterSpace pSpace = experiment.setup();

        experiment.runCrossValidation(pSpace);
        experiment.runTrainTest(pSpace);
    }

    protected ParameterSpace setup()
        throws Exception
    {
        String jsonPath = FileUtils.readFileToString(new File(
                "where/you/stored/your/Json/file"));
        JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonPath);

        return ParameterSpaceParser.createParamSpaceFromJson(json);
    }

    // ##### TRAIN-TEST #####
    protected void runTrainTest(ParameterSpace pSpace)
        throws Exception
    {

        // re-using the pre-configured BatchTask
        BatchTaskTrainTest batch = new BatchTaskTrainTest("TwentyNewsgroupsTrainTest",
                getPreprocessing());
        batch.setParameterSpace(pSpace);
        // remaining configuration...

        // Run
        Lab.getInstance().run(batch);
    }

    protected AnalysisEngineDescription getPreprocessing()
        throws ResourceInitializationException
    {
        return createEngineDescription(BreakIteratorSegmenter.class);
    }
}
```
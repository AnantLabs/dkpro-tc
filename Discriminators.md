In the following, all DKPro-TC discriminators ("Dimensions"), which have to be used (mandatory) or may be used (optional) are listed. You have to insert all mandatory and those optional discriminators you want to define into a _ParameterSpace_ where every task can access them. If various values should be tested for a parameter (e.g. various classification algorithms), you can simply specify a list with all the different values for that parameter.
We list each discriminator with the expected type of its value and a short explanation. For detailed examples on how to specify parameters, please have a look at the [demo experiments](DemoExperiments.md).


## Latest version ##
_if you use DKPro-TC version 0.5.0, see [here](Discriminators_0_5_0.md)_

### Mandatory Parameters ###

  * `readers` (DimensionBundle)
    * `readerTrain`: name of the reader class for the training data
    * `readerTrainParams`: a list of parameterName-value pairs to configure the training data reader
    * `readerTest`: name of the reader class for the test data (_not necessary for Crossvalidation_)
    * `readerTestParams`: a list of parameterName-value pairs to configure the test data reader (_not necessary for Crossvalidation_)
  * `featureSet`: a list of feature extractor class names (the feature extractors to use)
  * `pipelineParameters`: a list of parameterName-value pairs (parameters necessary to configure the feature extractors)
  * `dataWriter`: a DataWriter class (a writer to produce the input for the classification framework, e.g. Weka)
  * `classificationArguments`: a Weka/Meka classifier class and list of arguments to parametrize it (the classification algorithm)
  * `featureMode`: one of `document`, `unit`, `pair`, or `sequence`
  * `learningMode`: one of `singleLabel`, `multiLabel`, or `regression`

### Optional Parameters ###

  * `threshold`: boolean (the threshold to create a bipratition from a ranking; _only for multiLabel learning mode_)
  * `featureSelection` (DimensionBundle)
    * `attributeEvaluator`: Weka attribute selection evaluation class and list of arguments to parametrize it (the attribute evaluation algorithm)
    * `featureSearcher`: Weka attribute selection search class and list of arguments to parametrize it (the ranking algorithm; _only for singleLabel learning mode_)
    * `labelTransformationMethod`: a Mulan label transformation method (the label transformation method; _only for multiLabel learning mode_)
    * `numLabelsToKeep`: integer (the number of features which will be selected; _only for multiLabel learning mode_)
    * `applySelection`: boolean (whether to actually apply the )
  * `developerMode`: boolean (if true, you will not be warned when using feature extractors incompatible with the specified `featureMode`)
This tutorial shows how to create a custom feature extractor.

## Example ##

As example, we will have at look at the `EmoticonRatioExtractor`:

```
public class EmoticonRatioExtractor extends FeatureExtractorResource_ImplBase
   implements DocumentFeatureExtractor // the type of the feature extractor
{
   @Override
   public List<Feature> extract(JCas annoDb) throws TextClassificationException
   {
      // retrieve annotations from previously running preprocessing task
      double nrOfEmoticons = JCasUtil.select(annoDb, EMO.class).size();
      double nrOfTokens = JCasUtil.select(annoDb, Token.class).size();
      double ratio = nrOfEmoticons / nrOfTokens;
      return new Feature("EmoticonRatio", ratio).asList();
   }
}
```

## Options ##

Here is a list of options you have to specify a feature extractor according to your needs.

### Feature Modes ###

DKPro TC supports the following feature modes and feature extractor types:

| **Feature Mode** | **Corresponding Feature Extractor Type** | **Has Access To** |
|:-----------------|:-----------------------------------------|:------------------|
| document | DocumentFeatureExtractor | the entire CAS |
| pair | PairFeatureExtractor | both documents, in the form of two views on the same CAS |
| unit/sequence | ClassificationUnitFeatureExtractor | the current unit, all other units in the CAS, and to the entire CAS itself  |


### Feature Value Types ###

DKPro TC supports the following types of values for features:

| **Java Object** | **Corresponding Feature Value Type** |
|:----------------|:-------------------------------------|
| Boolean | boolean (true or false) |
| Number | a numeric value |
| Enum | a nominal value (from a finite set of strings) |
| String | a string value |
| MissingValue | a missing value for cases in which no value can be calculated (see below) |

### Missing Values ###

In certain cases, a feature extractor might not be able to calculate a value for a certain instance. In the following example, each document needs to contain at least one sentence. Otherwise, the feature cannot be calculated.

```
    @Override
    public List<Feature> extract(JCas jcas)
        throws TextClassificationException
    {
        List<Feature> featList = new ArrayList<Feature>();
        double numTokens = JCasUtil.select(jcas, Token.class).size();
        double numSentences = JCasUtil.select(jcas, Sentence.class).size();

        if (numSentences == 0) {
            featList.add(new Feature(FN_TOKENS_PER_SENTENCE, new MissingValue(
                    MissingValueNonNominalType.NUMERIC)));
        }
        else {
            featList.add(new Feature(FN_TOKENS_PER_SENTENCE, numTokens / numSentences));
        }
        return featList;
    }
```

For such cases, DKPro TC offers a `MissingValue`. You need to specify the feature value type for non-nominal types (see above), or, for nominal types, the enum class, e.g. `new MissingValue(Values.class)` for the enum `Values`. Missing values will be handled individually by each DataWriter.
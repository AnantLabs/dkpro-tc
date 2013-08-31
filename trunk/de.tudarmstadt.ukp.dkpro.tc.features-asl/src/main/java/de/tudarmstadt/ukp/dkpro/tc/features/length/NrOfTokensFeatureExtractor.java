package de.tudarmstadt.ukp.dkpro.tc.features.length;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.tc.api.features.ClasificationUnitFeatureExtractor;
import de.tudarmstadt.ukp.dkpro.tc.api.features.Feature;
import de.tudarmstadt.ukp.dkpro.tc.api.features.FeatureExtractorResource_ImplBase;
import de.tudarmstadt.ukp.dkpro.tc.exception.TextClassificationException;
import de.tudarmstadt.ukp.dkpro.tc.type.TextClassificationUnit;

@TypeCapability(inputs = { "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence",
        "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" })
public class NrOfTokensFeatureExtractor
    extends FeatureExtractorResource_ImplBase
    implements ClasificationUnitFeatureExtractor
{

    public static final String FN_NR_OF_TOKENS = "NrofTokens";
    public static final String FN_TOKENS_PER_SENTENCE = "NrofTokensPerSentence";

    @Override
    public List<Feature> extract(JCas jcas, TextClassificationUnit classificationUnit)
        throws TextClassificationException
    {
        List<Feature> featList = new ArrayList<Feature>();

        int numTokens;
        int numSentences;

        if (classificationUnit == null) {
            numTokens = JCasUtil.select(jcas, Token.class).size();
            numSentences = JCasUtil.select(jcas, Sentence.class).size();
        }
        else {
            numTokens = JCasUtil.selectCovered(jcas, Token.class, classificationUnit).size();
            numSentences = JCasUtil.selectCovered(jcas, Sentence.class, classificationUnit).size();
        }
        featList.addAll(Arrays.asList(new Feature(FN_NR_OF_TOKENS, numTokens)));
        if (numSentences > 0) {
            featList.addAll(Arrays.asList(new Feature(FN_TOKENS_PER_SENTENCE, (double) numTokens
                    / numSentences)));
        }
        return featList;
    }
}
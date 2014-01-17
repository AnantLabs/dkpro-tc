package de.tudarmstadt.ukp.dkpro.tc.features.style;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.tc.api.features.DocumentFeatureExtractor;
import de.tudarmstadt.ukp.dkpro.tc.api.features.Feature;
import de.tudarmstadt.ukp.dkpro.tc.api.features.IFeature;
import de.tudarmstadt.ukp.dkpro.tc.api.features.FeatureExtractorResource_ImplBase;
import de.tudarmstadt.ukp.dkpro.tc.exception.TextClassificationException;

public class TypeTokenRatioFeatureExtractor
    extends FeatureExtractorResource_ImplBase
    implements DocumentFeatureExtractor
{
    public static final String FN_TTR = "TypeTokenRatio";

    @Override
    public List<IFeature> extract(JCas jcas)
        throws TextClassificationException
    {

        FrequencyDistribution<String> fd = new FrequencyDistribution<String>();

        for (Token token : JCasUtil.select(jcas, Token.class)) {
            fd.inc(token.getCoveredText().toLowerCase());
        }
        double ttr = 0.0;
        if (fd.getN() > 0) {
            ttr = (double) fd.getB() / fd.getN();
        }

        List<IFeature> featList = new ArrayList<IFeature>();
        featList.add(new Feature(FN_TTR, ttr));

        return featList;
    }
}
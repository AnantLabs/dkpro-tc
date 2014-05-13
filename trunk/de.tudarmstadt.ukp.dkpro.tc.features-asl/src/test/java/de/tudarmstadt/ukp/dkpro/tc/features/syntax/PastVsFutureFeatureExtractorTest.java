package de.tudarmstadt.ukp.dkpro.tc.features.syntax;

import static de.tudarmstadt.ukp.dkpro.tc.api.features.util.FeatureTestUtil.assertFeature;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.tc.api.features.Feature;

public class PastVsFutureFeatureExtractorTest
{
    @Test
    public void pastVsFutureFeatureExtractorTest()
        throws Exception
    {
        AnalysisEngineDescription desc = createEngineDescription(
                createEngineDescription(BreakIteratorSegmenter.class),
                createEngineDescription(OpenNlpPosTagger.class, OpenNlpPosTagger.PARAM_LANGUAGE,
                        "en"));
        AnalysisEngine engine = createEngine(desc);

        JCas jcas = engine.newJCas();
        jcas.setDocumentLanguage("en");
        jcas.setDocumentText("They tested. We test. She tests. You will test.");
        engine.process(jcas);

        PastVsFutureFeatureExtractor extractor = new PastVsFutureFeatureExtractor();
        List<Feature> features = extractor.extract(jcas);

        Assert.assertEquals(3, features.size());
        Iterator<Feature> iter = features.iterator();
        assertFeature(PastVsFutureFeatureExtractor.FN_PAST_RATIO, 25.0, iter.next());
        assertFeature(PastVsFutureFeatureExtractor.FN_FUTURE_RATIO, 75.0, iter.next());
        assertFeature(PastVsFutureFeatureExtractor.FN_FUTURE_VS_PAST_RATIO, 3.0, iter.next());

    }
}
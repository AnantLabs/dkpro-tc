package de.tudarmstadt.ukp.dkpro.tc.demo.regression.io;

import static org.junit.Assert.assertEquals;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.tc.core.io.AbstractPairReader;

public class STSReaderTest
{

    @Test
    public void stsReaderTest()
            throws Exception
    {
        CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
                STSReader.class,
                STSReader.PARAM_INPUT_FILE, "src/test/resources/sts/STS.input.MSRpar.txt",
                STSReader.PARAM_GOLD_FILE, "src/test/resources/sts/STS.gs.MSRpar.txt"
        );

        int i=0;
        for (JCas jcas : new JCasIterable(reader)) {
            
            System.out.println(jcas.getView(AbstractPairReader.PART_ONE).getDocumentText());
            System.out.println(jcas.getView(AbstractPairReader.PART_TWO).getDocumentText());
            i++;
        }
        assertEquals(10, i);
    }
}
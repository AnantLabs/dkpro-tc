# DKPro TC 0.6.0-Release #

## Highlights ##

  * Release on Maven Central
  * [Issue 107](http://code.google.com/p/dkpro-tc/issues/detail?id=107): Support sequence labeling (with Mallet)
  * [Issue 97](http://code.google.com/p/dkpro-tc/issues/detail?id=97): Enable Modes for Learning and Feature Types
  * NGram and PairNGram modules have been entirely re-written
  * Demo modules have been re-ordered: separated modules for Groovy and Java setup
  * Upgrade to Java 7

## Enhancements ##

  * Support for SkipNGrams
  * [Issue 117](http://code.google.com/p/dkpro-tc/issues/detail?id=117): Coverage of source code comments has been increased
  * [Issue 79](http://code.google.com/p/dkpro-tc/issues/detail?id=79): Support for Missing Values in Feature Extractors
  * [Issue 24](http://code.google.com/p/dkpro-tc/issues/detail?id=24): Bag of sounds feature extractor
  * [Issue 90](http://code.google.com/p/dkpro-tc/issues/detail?id=90): Add data writers for testing
  * [Issue 104](http://code.google.com/p/dkpro-tc/issues/detail?id=104): Add PerformanceBatchReport
  * Increased number of demos (e.g. for unit classification)
  * Added [BatchTaskCrossValidationWithFoldControl](CVWithFoldControl.md)

## Major Version Enhancements ##

  * Upgrade to DKPro Lab 0.11.0
  * Upgrade to DKPro Core 1.6.1
  * Upgrade to Apache uimaFIT 2.1.0
  * Upgrade to Meka 1.6.2
  * Upgrade to Groovy 2.2.2

## Other Issues Fixed ##

  * [Issue 80](http://code.google.com/p/dkpro-tc/issues/detail?id=80): Feature extractors returning empty lists can cause errors
  * [Issue 84](http://code.google.com/p/dkpro-tc/issues/detail?id=84): DocumentFeatureExtractor and ClassificationUnitFeatureExtractor are used inconsistently
  * [Issue 94](http://code.google.com/p/dkpro-tc/issues/detail?id=94): Setting number of folds accidentally to 1 causes exception
  * [Issue 105](http://code.google.com/p/dkpro-tc/issues/detail?id=105): FeatureValuesReport broken
  * [Issue 106](http://code.google.com/p/dkpro-tc/issues/detail?id=106): Throw Exception when mode-incompatible FE is encountered
  * [Issue 116](http://code.google.com/p/dkpro-tc/issues/detail?id=116): Improve format of the output by OutcomeIDReport
  * [Issue 118](http://code.google.com/p/dkpro-tc/issues/detail?id=118): Rename preprocessing aggregate
  * [Issue 131](http://code.google.com/p/dkpro-tc/issues/detail?id=131): NrOfTokensFEs cause an exception in FeatureExtractionTask when at least one but not all documents contain valid sentence annotations
  * [Issue 133](http://code.google.com/p/dkpro-tc/issues/detail?id=133): Phonetic ngrams in POS ngram DFE crash on some characters
  * [Issue 142](http://code.google.com/p/dkpro-tc/issues/detail?id=142): Some Lucene NGram Meta Collectors don't record term counts in a document
  * [Issue 152](http://code.google.com/p/dkpro-tc/issues/detail?id=152): Naming scheme of ArtifactIds changed to the simpler, dashed Maven standard. Furthermore, license suffixes have been removed from ArtifactIDs; e.g. `dkpro-tc-core` instead of `de.tudarmstadt.ukp.dkpro.tc.core-asl`. GroupIDs and class names remain unchanged.
  * [Issue 156](http://code.google.com/p/dkpro-tc/issues/detail?id=156): Adding license headers to all classes

## Other ##
  * Module `dkpro-tc-features-pair-similarity` has been excluded from the Maven Central release, since the DKPro Similarity dependency is not yet available on Central.
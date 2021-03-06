package edu.stanford.nlp.international;

import edu.stanford.nlp.parser.lexparser.ArabicTreebankParserParams;
import edu.stanford.nlp.parser.lexparser.ArabicUTMTreebankParserParams;
import edu.stanford.nlp.parser.lexparser.ChineseTreebankParserParams;
import edu.stanford.nlp.parser.lexparser.EnglishTreebankParserParams;
import edu.stanford.nlp.parser.lexparser.FrenchTreebankParserParams;
import edu.stanford.nlp.parser.lexparser.HebrewTreebankParserParams;
import edu.stanford.nlp.parser.lexparser.NegraPennTreebankParserParams;
import edu.stanford.nlp.parser.lexparser.TreebankLangParserParams;

/**
 * Constants and parameters for multilingual parsing.
 * <p>
 * TODO(spenceg): "ArabicUTM" is an experimental configuration for the CL 2012 submission on parsing for MWEs. 
 * It can be safely removed after the experiments are finished.
 *  
 * @author Spence Green
 *
 */
public class Languages {

  private Languages() {
  }


  public static enum Language {Arabic,ArabicUTM,Chinese,English,German,French,Hebrew}
  private static final StringBuilder langList = new StringBuilder();
  static {
    for(Language lang : Language.values()) {
      langList.append(lang.toString());
      langList.append(" ");
    }
  }

  public static String listOfLanguages() {
    return langList.toString().trim();
  }

  public static TreebankLangParserParams getLanguageParams(String lang) {
    return getLanguageParams(Language.valueOf(lang));
  }

  public static TreebankLangParserParams getLanguageParams(Language lang) {
    TreebankLangParserParams tlpp; // initialized below
    switch(lang) {
    case Arabic:
      tlpp = new ArabicTreebankParserParams();
      break;
    
    case ArabicUTM:
      tlpp = new ArabicUTMTreebankParserParams();
      break;

    case Chinese:
      tlpp = new ChineseTreebankParserParams();
      break;

    case German:
      tlpp = new NegraPennTreebankParserParams();
      break;

    case French:
      tlpp = new FrenchTreebankParserParams();
      break;

    case Hebrew:
      tlpp = new HebrewTreebankParserParams();
      break;

    default:
      tlpp = new EnglishTreebankParserParams();
    }
    return tlpp;
  }
}

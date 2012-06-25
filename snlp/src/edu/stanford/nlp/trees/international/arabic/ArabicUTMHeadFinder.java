package edu.stanford.nlp.trees.international.arabic;

import java.util.HashMap;
import java.util.regex.Pattern;

import edu.stanford.nlp.trees.AbstractCollinsHeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;

/**
 * Find the head of an Arabic tree, using the usual kind of heuristic
 * head finding rules.
 * <p>
 * <i>Implementation notes.</i>
 * TO DO: make sure that -PRD marked elements are always chosen as heads.
 * (Has this now been successfully done or not??)
 * <p>
 * Mona: I added the 8 new Nonterm for the merged DT with its following
 * category as a rule the DT nonterm is right headed, the 8 new nonterm DTs
 * are: DTCD, DTRB, DTRP, DTJJ, DTNN, DTNNS, DTNNP, DTNNPS.
 * This was added Dec 7th, 2004.
 *
 * @author Roger Levy
 * @author Mona Diab
 * @author Christopher Manning (added new stuff for ATBp3v3
 */
public class ArabicUTMHeadFinder extends AbstractCollinsHeadFinder {
  private static final long serialVersionUID = 6203368998430280740L;
  protected TagSet tagSet;

  /* A work in progress. There may well be a better way to parameterize the HeadFinders via tagset. */
  public enum TagSet {
    ORIGINAL {
      @Override
      String prep()  { return "PREP"; }
      @Override
      String noun()  { return "NOUN"; }
      @Override
      String det()  { return "DET"; }
      @Override
      String adj()  { return "ADJ"; }
      @Override
      TreebankLanguagePack langPack()  { return new ArabicTreebankLanguagePack(); }
    };

    abstract String prep();
    abstract String noun();
    abstract String adj();
    abstract String det();
    abstract TreebankLanguagePack langPack();

    static TagSet tagSet(String str) {
      if(str.equals("ORIGINAL"))
        return ORIGINAL;
      else throw new IllegalArgumentException("Don't know anything about tagset " + str);
    }
  }

  public ArabicUTMHeadFinder() {
    this(new ArabicUTMTreebankLanguagePack());
  }

  /**
   * Construct an ArabicHeadFinder with a String parameter corresponding to the tagset in use.
   * @param tagSet Either "ORIGINAL" or "BIES_COLLAPSED"
   */
  public ArabicUTMHeadFinder(String tagSet) {
    this(TagSet.tagSet(tagSet));
  }

  public ArabicUTMHeadFinder(TagSet tagSet) {
    this(tagSet.langPack(), tagSet);
    //this(new ArabicTreebankLanguagePack(), tagSet);
  }

  public ArabicUTMHeadFinder(TreebankLanguagePack tlp) {
    this(tlp,TagSet.ORIGINAL);
  }

  protected ArabicUTMHeadFinder(TreebankLanguagePack tlp, TagSet tagSet) {
    super(tlp);
    this.tagSet = tagSet;
    //System.err.println("##testing: noun tag is " + tagSet.noun());

    nonTerminalInfo = new HashMap<String,String[][]>();

    nonTerminalInfo.put("NX", new String[][]{{"left", "DET", tagSet.noun()}});
    nonTerminalInfo.put("ADJP", new String[][]{{"rightdis", tagSet.adj()}, {"right", "ADJP","MWADJP", "VERB", tagSet.noun(), "NOFUNC", "NO_FUNC"}, {"right", "PRT", "NUM"}, {"right", "DET"}}); // sometimes right, sometimes left headed??
    nonTerminalInfo.put("MWADJP", new String[][]{{"rightdis", tagSet.adj()}, {"right", "ADJP","MWADJP", "VERB", tagSet.noun(), "NOFUNC", "NO_FUNC"}, {"right", "PRT", "NUM"}, {"right", "DET"}}); // sometimes right, sometimes left headed??
    nonTerminalInfo.put("ADVP", new String[][]{{"left", "ADV", "ADVP","MWADVP", "WHADVP"}, {"left", "NUM", "PRT", tagSet.noun(), "CONJ", tagSet.adj(), "ADP", "NP", "MWNP", "NOFUNC"}}); // NNP is a gerund that they called an unknown (=NNP, believe it or not...)
    nonTerminalInfo.put("MWADVP", new String[][]{{"left", "ADV", "ADVP","MWADVP", "WHADVP"}, {"left", "NUM", "PRT", tagSet.noun(), "CONJ", tagSet.adj(), "ADP", "NP", "MWNP", "NOFUNC"}}); // NNP is a gerund that they called an unknown (=NNP, believe it or not...)
    nonTerminalInfo.put("CONJP", new String[][]{{"right", "ADP", "PRT", tagSet.noun()}});
    nonTerminalInfo.put("MWCONJP", new String[][]{{"right", "ADP", "PRT", tagSet.noun()}});
    nonTerminalInfo.put("FRAG", new String[][]{{"left", tagSet.noun()}, {"left", "VERB"}});
    nonTerminalInfo.put("MWFRAG", new String[][]{{"left", tagSet.noun()}, {"left", "VERB"}});
    nonTerminalInfo.put("INTJ", new String[][]{{"left", "PRT"}});
    nonTerminalInfo.put("LST", new String[][]{{"left"}});
    nonTerminalInfo.put("NAC", new String[][]{{"left", "NP","MWNP", "SBAR","MWSBAR", "PP","MWPP","ADJP","MWADJP", "S","MWS", "PRT", "UCP"}, {"left", "ADVP","MWADVP"}}); // note: maybe CC, RB should be the heads?
    nonTerminalInfo.put("NP", new String[][]{{"left", tagSet.noun(), "NP","MWNP", "PRON", "WHNP", "QP", "PRT", "NOFUNC", "NO_FUNC"}, {"left", tagSet.adj()}, {"right", "NUM"}, {"left", "PRON"}, {"right", "DET"}}); // should the JJ rule be left or right?
    nonTerminalInfo.put("MWNP", new String[][]{{"left", tagSet.noun(), "NP","MWNP", "PRON", "WHNP", "QP", "PRT", "NOFUNC", "NO_FUNC"}, {"left", tagSet.adj()}, {"right", "NUM"}, {"left", "PRON"}, {"right", "DET"}}); // should the JJ rule be left or right?
    nonTerminalInfo.put("PP", new String[][]{{"left", tagSet.prep(), "PP","MWPP","PRT", "X"}, {"left", tagSet.noun(), "PRT"}, {"left", "NP","MWNP"}}); // NN is for a mistaken "fy", and many wsT
    nonTerminalInfo.put("MWPP", new String[][]{{"left", tagSet.prep(), "PP","MWPP","PRT", "X"}, {"left", tagSet.noun(), "PRT"}, {"left", "NP","MWNP"}}); // NN is for a mistaken "fy", and many wsT
    nonTerminalInfo.put("PRN", new String[][]{{"left", "NP","MWNP"}}); // don't get PUNC
    nonTerminalInfo.put("MWPRN", new String[][]{{"left", "NP","MWNP"}}); // don't get PUNC
    nonTerminalInfo.put("PRT", new String[][]{{"left", "PRT", "ADP"}});
    nonTerminalInfo.put("QP", new String[][]{{"right", "NUM", tagSet.noun(), tagSet.adj()}});

    nonTerminalInfo.put("S", new String[][]{{"left", "VP", "MWVP", "S", "MWS"}, {"right", "PP","MWPP","ADVP","MWADVP", "SBAR","MWSBAR", "UCP", "ADJP","MWADJP"}}); // really important to put in -PRD sensitivity here!
    nonTerminalInfo.put("MWS", new String[][]{{"left", "VP", "MWVP", "S", "MWS"}, {"right", "PP","MWPP","ADVP","MWADVP", "SBAR","MWSBAR", "UCP", "ADJP","MWADJP"}}); // really important to put in -PRD sensitivity here!
    nonTerminalInfo.put("SQ", new String[][]{{"left", "VP", "MWVP", "PP","MWPP"}}); // to be principled, we need -PRD sensitivity here too.
    nonTerminalInfo.put("SBAR", new String[][]{{"left", "WHNP", "WHADVP", "ADV", "PRT", "ADP", "SBAR","MWSBAR", "CONJ", "PRT", "WHPP", "ADVP","MWADVP", "PRT", "ADV", "X"}, {"left", tagSet.noun()}, {"left", "S","MWS"}});
    nonTerminalInfo.put("MWSBAR", new String[][]{{"left", "WHNP", "WHADVP", "ADV", "PRT", "ADP", "SBAR","MWSBAR", "CONJ", "PRT", "WHPP", "ADVP","MWADVP", "PRT", "ADV", "X"}, {"left", tagSet.noun()}, {"left", "S","MWS"}});
    nonTerminalInfo.put("SBARQ", new String[][]{{"left", "WHNP", "WHADVP", "PRT", "ADP", "SBAR","MWSBAR", "CONJ", "PRT", "WHPP", "ADVP","MWADVP", "PRT", "ADV", "X"}, {"left", tagSet.noun()}, {"left", "S","MWS"}}); // copied from SBAR rule -- look more closely when there's time
    nonTerminalInfo.put("UCP", new String[][]{{"left"}});
    nonTerminalInfo.put("VP", new String[][]{{"left", "VERB", "VP", "MWVP", "ADV", "X"}, {"left", "ADP"}, {"left", "NOFUNC", tagSet.noun()}}); // exclude RP because we don't want negation markers as heads -- no useful information?
    nonTerminalInfo.put("MWVP", new String[][]{{"left", "VERB", "VP", "MWVP", "ADV", "X"}, {"left", "ADP"}, {"left", "NOFUNC", tagSet.noun()}}); // exclude RP because we don't want negation markers as heads -- no useful information?

    
    //also, RB is used as gerunds

    nonTerminalInfo.put("WHADVP", new String[][]{{"left", "ADV", "PRT"}, {"right", "CONJ"}, {"left", "ADP"}});
    nonTerminalInfo.put("WHNP", new String[][]{{"right", "PRT"}});
    nonTerminalInfo.put("WHPP", new String[][]{{"left",  "ADP", "ADV"}});
    nonTerminalInfo.put("X", new String[][]{{"left"}});

    // stand-in dependency:
    nonTerminalInfo.put("EDITED", new String[][]{{"left"}});
    nonTerminalInfo.put(tlp.startSymbol(), new String[][]{{"left"}});

    // one stray SINV in the training set...garbage head rule here.
    nonTerminalInfo.put("SINV", new String[][]{{"left","ADJP","MWADJP","VP","MWVP"}});
  }


  private final Pattern predPattern = Pattern.compile(".*-PRD$");

  /**
   * Predicatively marked elements in a sentence should be noted as heads
   */
  @Override
  protected Tree findMarkedHead(Tree t) {
    String cat = t.value();
    if (cat.matches("S|MWS")) {
      Tree[] kids = t.children();
      for (Tree kid : kids) {
        if (predPattern.matcher(kid.value()).matches()) {
          return kid;
        }
      }
    }
    return null;
  }

}

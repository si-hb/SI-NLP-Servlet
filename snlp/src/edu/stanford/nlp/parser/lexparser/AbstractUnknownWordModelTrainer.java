package edu.stanford.nlp.parser.lexparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Index;


public abstract class AbstractUnknownWordModelTrainer 
  implements UnknownWordModelTrainer 
{
  double treesRead;
  double totalTrees;

  Index<String> wordIndex, tagIndex;

  Options op;
  Lexicon lex;

  @Override
  public void initializeTraining(Options op, Lexicon lex, 
                                 Index<String> wordIndex, 
                                 Index<String> tagIndex, double totalTrees) {
    this.totalTrees = totalTrees;
    this.treesRead = 0;

    this.wordIndex = wordIndex;
    this.tagIndex = tagIndex;
    this.op = op;
    this.lex = lex;
  }



  public final void train(Collection<Tree> trees) {
    train(trees, 1.0);
  }

  public final void train(Collection<Tree> trees, double weight) {
    for (Tree tree : trees) {
      train(tree, weight);
    }
  }


  public final void train(Tree tree, double weight) {
    incrementTreesRead(weight);
    int loc = 0;
    for (TaggedWord tw : tree.taggedYield()) {
      train(tw, loc, weight);
      ++loc;
    }
  }

  public void incrementTreesRead(double weight) {
    treesRead += weight;
  }
}

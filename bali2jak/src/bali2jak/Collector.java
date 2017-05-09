package bali2jak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

//-----------------------------------------------------------------------//
// Collector class and classes for collected elements:
//-----------------------------------------------------------------------//

/**
 * Extends {@link AstVisitor} to provide data collection methods for those
 * parts of the parse tree(s) that will be composed and translated.  For
 * composition of parse trees, build an empty <code>Collector</code> and
 * apply it to all trees in order of composition, starting with the
 * <em>base</em> tree.  For translation of a single parse tree, apply a
 * single empty <code>Collector</code> to the parse tree.
 *
 * @layer<collect>
 */
    
public class Collector extends  AstVisitor {

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // This section initializes data structures built by visitation:
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    /**
     * Bali rules are aggregated by rule name as a list of productions in
     * order of declaration.
     *
     * @layer<collect>
     */
    final public  BaliRulesData
        baliRules = new  BaliRulesData() ;

    /**
     * Bali tokens are aggregated by name where later definitions of a name
     * override the previous.
     *
     * @layer<collect>
     */
    final public  BaliTokensData
        baliTokens = new  BaliTokensData() ;

    /**
     * Java code blocks (handwritten parsing methods) are aggregated as a
     * list in order of declaration.
     *
     * @layer<collect>
     */
    final public  JavaBlocksData
        javaBlocks = new  JavaBlocksData() ;

    /**
     * Option code blocks (for generated JavaCC code) are aggregated as a
     * list in order of declaration.
     *
     * @layer<collect>
     */
    final public  OptionBlocksData
        optionBlocks = new  OptionBlocksData() ;

    /**
     * Parser code blocks (to be appended to generated JavaCC parser) are
     * aggregated as a list in order of declaration.
     *
     * @layer<collect>
     */
    final public  ParserBlocksData
        parserBlocks = new  ParserBlocksData() ;

    /**
     * Regular expression token definitions are aggregated by (states,kind)
     * as a list of definitions in order of declaration.
     *
     * @layer<collect>
     */
    final public  RegexTokensData
        regexTokens = new  RegexTokensData() ;

    /**
     * Token manager code blocks (for inclusion with generated JavaCC token
     * manager) are aggregated as a list in order of declaration.
     *
     * @layer<collect>
     */
    final public  TokenManagerData
        tknMgrBlocks = new  TokenManagerData() ;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // The "visit" methods follow.  Their implementation is all of the form
    // <code>data.addNode(node)</code> where <code>data</code> is one of
    // the above data collection objects.  See the actual code in the data
    // collection classes; these are implemented using the layering scheme,
    // so they may be refined in later layers.
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    public void visit( BaliTokenDefineNode node ) {
        baliTokens.addNode( node ) ;
    }

    public void visit( JavacodeNode node ) {
        javaBlocks.addNode( node ) ;
    }

    public void visit( OptionsNode node ) {
        optionBlocks.addNode( node ) ;
    }

    public void visit( ParserCodeNode node ) {
        parserBlocks.addNode( node ) ;
    }

    public void visit( RegexDefinitionNode node ) {
        regexTokens.addNode( node ) ;
    }

    public void visit( TokenManagerNode node ) {
        tknMgrBlocks.addNode( node ) ;
    }

    public void visit( BaliGrammarNode node ) {
        baliRules.addNode( node ) ;
        baliRules.addDefinition( node.tok[0].getTokenName() ) ;

        // Visit sub-trees to capture rule references:
        //
        visit( ( AstNode ) node ) ;
    }

    public void visit( IdentifierNode node ) {
        baliRules.addReference( node.tok[0].getTokenName() ) ;
    }

    public void visit( RequireRuleNode node ) {

        String ruleName = node.tok[0].getTokenName() ;
        baliRules.addRequire( ruleName ) ;

        RequireTypeNode type = ( RequireTypeNode ) node.arg[0].arg [0] ;
        if ( type == null )
            return ;

        baliRules.setType( ruleName, type.tok[1].getTokenName() ) ;
    }

    public void generateNonterminals( File directory ) {
        Main.DEBUG.entering( "bali2jak.Collector","generateNonterminals" ) ;
        baliRules.generateClasses( directory, layerName ) ;
        Main.DEBUG.exiting( "bali2jak.Collector","generateNonterminals" ) ;
    }

    public void setLayer( String layerName ) {
        this.layerName = layerName ;
    }

    private String layerName = null ;

}

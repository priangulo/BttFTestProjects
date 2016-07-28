MixinSPL : Parser Lang [ ASTMixin ] BaseError Rest :: CplMain ;

Parser : kernel ;  // implicit syntax in default feature PARSER

Rest : mixinbase LocalId AspectDecl mixinClass mixinInt [ MixinSm ] antCompose :: RRule ;

BaseError : CommonBase CommonError :: BERule ; 

ASTMixin : CommonErrorAst LocalIdAst :: CodeQuotes ;

Lang : Java Comments StringAST :: LRule ;

MixinSm : mixinSm :: StateMachines ;

%% // no non-grammar constraints

MixinSm implies ASTMixin ;


// annotations


/*

From AHEAD equations created at build time:

mixin=parser lang ast-mixin base+error dsl/java/mixinbase dsl/java/LocalId dsl/java/AspectDecl dsl/java/mixinClass dsl/java/mixinInt dsl/java/mixinSm dsl/support/antCompose

parser=dsl/kernel build/java/syntax

lang=dsl/java/Java dsl/java/Comments dsl/java/StringAST

ast-mixin=dsl/java/CommonErrorAst dsl/java/LocalIdAst

base+error=dsl/java/CommonBase dsl/java/CommonError


---------

From AHEAD Documentation:

all  = java AST BASE sm COMPOSE compose-sm ant
cat  = java     BASE sm COMPOSE compose-sm ant
utcs = java AST BASE    COMPOSE            ant

where:

AST     = ast gscope
BASE    = base source local-id layer
COMPOSE = compose-class compose-int
*/

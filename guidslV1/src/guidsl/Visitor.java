package guidsl;

interface Visitor {

    void action(AExpr n);

    void action(AstList n);

    void action(AstListNode n);

    void action(AstNode n);

    void action(AstOptNode n);

    void action(Avar n);

    void action(AvarList n);

    void action(BAnd n);

    void action(BChoose1 n);

    void action(BExpr n);

    void action(BIff n);

    void action(BImplies n);

    void action(BNot n);

    void action(BOr n);

    void action(Bvar n);

    void action(Cons n);

    void action(ConsStmt n);

    void action(EExpr n);

    void action(ESList n);

    void action(EStmt n);

    void action(Expr n);

    void action(ExprList n);

    void action(ExprStmt n);

    void action(GPattern n);

    void action(GProd n);

    void action(GProduction n);

    void action(GTerm n);

    void action(IExpr n);

    void action(MainModel n);

    void action(Model n);

    void action(NExpr n);

    void action(OExpr n);

    void action(Opt n);

    void action(Optid n);

    void action(Opts n);

    void action(OptTerm n);

    void action(Paren n);

    void action(Pat n);

    void action(Pats n);

    void action(PlusTerm n);

    void action(Prods n);

    void action(SimplePattern n);

    void action(StarTerm n);

    void action(Strlit n);

    void action(TermList n);

    void action(TermName n);

    void action(Var n);

    void action(Vars n);

    void action(VarStmt n);
}

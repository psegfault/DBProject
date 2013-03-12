package edu.buffalo.cse.sql.test;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import edu.buffalo.cse.sql.Schema;
import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.plan.ExprTree;
import edu.buffalo.cse.sql.plan.PlanNode;
import edu.buffalo.cse.sql.plan.NullSourceNode;
import edu.buffalo.cse.sql.plan.ProjectionNode;
import edu.buffalo.cse.sql.plan.SelectionNode;
import edu.buffalo.cse.sql.plan.ScanNode;
import edu.buffalo.cse.sql.plan.JoinNode;
import edu.buffalo.cse.sql.plan.UnionNode;
import edu.buffalo.cse.sql.plan.AggregateNode;
public class CONST15 extends TestHarness {
  public static void main(String args[]) {
    TestHarness.main(args, new CONST15());
  }
  public void testRA() {
    Map<String, Schema.TableFromFile> tables
      = new HashMap<String, Schema.TableFromFile>();
    NullSourceNode child_1 = new NullSourceNode(1);
    ProjectionNode query_0 = new ProjectionNode();
    query_0.addColumn(new ProjectionNode.Column("EXPR", new ExprTree(ExprTree.OpCode.MULT, new ExprTree.ConstLeaf(1), new ExprTree.ConstLeaf(2))));
    query_0.setChild(child_1);
    TestHarness.testQuery(tables, getResults0(), query_0);
    System.out.println("Passed RA Test CONST15");
  }
  public void testSQL() {
    List<List<Datum[]>> expected = new ArrayList<List<Datum[]>>();
    expected.add(getResults0());
    TestHarness.testProgram(new File("test/CONST15.SQL"),
                            expected);
    System.out.println("Passed SQL Test CONST15");
  }
  ArrayList<Datum[]> getResults0() {
    ArrayList<Datum[]> ret = new ArrayList<Datum[]>();
    ret.add(new Datum[] {new Datum.Int(2)}); 
    return ret;
  }
}

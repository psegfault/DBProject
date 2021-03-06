/* Generated By:JavaCC: Do not edit this line. Sql.java */
package edu.buffalo.cse.sql;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;

import edu.buffalo.cse.sql.Schema;
import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.operations.AggregateOperator;
import edu.buffalo.cse.sql.operations.JoinOperator;
import edu.buffalo.cse.sql.operations.ProjectOperation;
import edu.buffalo.cse.sql.operations.ScanOperator;
import edu.buffalo.cse.sql.operations.SelectOperation;
import edu.buffalo.cse.sql.operations.UnionOperator;
import edu.buffalo.cse.sql.parser.Parser;
import edu.buffalo.cse.sql.plan.AggregateNode;
import edu.buffalo.cse.sql.plan.ExprTree;
import edu.buffalo.cse.sql.plan.JoinNode;
import edu.buffalo.cse.sql.plan.NullSourceNode;
import edu.buffalo.cse.sql.plan.PlanNode;
import edu.buffalo.cse.sql.plan.ProjectionNode;
import edu.buffalo.cse.sql.plan.ScanNode;
import edu.buffalo.cse.sql.plan.SelectionNode;
import edu.buffalo.cse.sql.plan.UnionNode;


public class Sql {

  public static void main( String[] args )
  {
    System.out.println("You haven't implemented me yet!");
  }

  public static List<Datum[]> execQuery(Map<String, Schema.TableFromFile> tables,PlanNode q)throws SqlException
    { 
	  
	  switch(q.struct){
	  case LEAF:
		  switch(q.type){
		  case SCAN:
			  ScanNode node=(ScanNode)q;
			  ScanOperator sop = new ScanOperator(node);
			  return sop.getTuples(tables);
			  //break;
		  case NULLSOURCE:
			  NullSourceNode nNode=(NullSourceNode)q;
			  return new ArrayList<Datum[]>();
			  //break;
		  }
		  break;
	  case UNARY:
		  
		  switch (q.type) {
		case AGGREGATE:
			AggregateNode aggrNode = (AggregateNode)q;
			List<Datum[]> aggrResult=execQuery(tables,aggrNode.getChild());
			AggregateOperator aop=new AggregateOperator(aggrNode,aggrResult);
			return aop.getTuples();
			//break;
		case SELECT:
			SelectionNode selectNode=(SelectionNode)q;
			List<Datum[]> selectResult=execQuery(tables,selectNode.getChild());
			SelectOperation slop = new SelectOperation(selectNode);
			return slop.getTuples(selectResult,selectNode.getCondition());
			//break;
		case PROJECT:
			ProjectionNode projNode=(ProjectionNode)q;
			List<Datum[]> projResult=execQuery(tables,projNode.getChild());
			ProjectOperation prop = new ProjectOperation(projNode);
			ExprTree e = null;
			return prop.getTuples(projResult,e);
			//break;
		}
		  break;
	  case BINARY:
		  switch (q.type) {
		case JOIN:
			JoinNode joinNode = (JoinNode)q;
			JoinOperator jop = new JoinOperator(joinNode);
			List<Datum[]> lResult=execQuery(tables,joinNode.getLHS());
			List<Datum[]> rResult=execQuery(tables,joinNode.getRHS());
			 return jop.getTuples(lResult,rResult);
			//break;
		case UNION:
			UnionNode unionNode=(UnionNode)q;
			UnionOperator uop=new UnionOperator(unionNode);
			List<Datum[]> lUResult=execQuery(tables,unionNode.getLHS());
			List<Datum[]> rUResult=execQuery(tables,unionNode.getRHS());
			return uop.getTuples(lUResult,rUResult);
			//break;
		default:
			break;
		}
	  }
	  throw new SqlException("execQuery() is unimplemented"); 
    }

  public static List<List<Datum[]>> execFile(File program)
    throws SqlException
    { 
	  
	  List<List<Datum[]>> listResult= new ArrayList<List<Datum[]>>();
	  try {
	  Parser newParser=new Parser(new FileInputStream(program));	  
		Map<String,Object> result=newParser.parseSQL();
		Map<String,Schema.TableFromFile> tables=(Map<String, Schema.TableFromFile>) result.get("Tables");
		List<PlanNode> planNodes=(List<PlanNode>) result.get("Roots");
		for(PlanNode node:planNodes) {
			listResult.add(execQuery(tables, node));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	  return listResult;
	  
}
}
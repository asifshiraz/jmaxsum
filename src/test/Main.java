/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import boundedMaxSum.BoundedMaxSum;
import boundedMaxSum.InstanceCloner;
import hacks.ScrewerUp;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;

import exception.InvalidInputFileException;
import exception.PostServiceNotSetException;
import maxsum.MS_COP_Instance;
import misc.Utils;
import system.COP_Instance;
import olimpo.Cerberus;
import olimpo.Athena;
import olimpo.Eris;
import operation.Solver;
import powerGrid.PowerGrid;

/**
 * Class used for test executions.
 * @author Michele Roncalli
 */
public class Main {

    static final int debug = test.DebugVerbosity.debugMain;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Started");

        /*double res = Math.exp(-1*Double.POSITIVE_INFINITY/200);
        System.out.println("res: " +res);
        System.exit(0);*/

        try {

            String[] paths = {
            	//"D:\\EclipseWorkspace\\jmaxsum\\copTest.cop2",
                "D:\\EclipseWorkspace\\jmaxsum\\RoverCop.cop2",
                //"/home/mik/NetBeansProjects/maxSum/paper.cop2",
                //"/home/mik/NetBeansProjects/maxSum/paper_multi.cop2",
                //"/home/mik/NetBeansProjects/maxSum/simpleTest.cop2",
                //"/home/mik/NetBeansProjects/maxSum/test43.cop2",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/test43_mod.cop2",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/bounded_simple.cop2",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/infinity_test3.cop2",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/test_infinity_4.cop2",
                //"/home/mik/Documenti/univr/Ragionamento Automatico/stage/report/DCOPProblem/Node200Domain3InducedWidth2Den2.0e-2-30.dcop"
                //"/home/mik/Documenti/univr/Ragionamento Automatico/stage/report/DCOPProblem/Node20Domain03InducedWidth5Den0.1-01.dcop",
                //"D:\\EclipseWorkspace\\jmaxsum\\232.pg"
                //"/home/mik/NetBeansProjects/jMaxSumSVN/report/2/0.25/0.pg",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/232.pg",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/dcopInput.cop",
            };

            for (String path : paths) {

                /*if (debug >= 1) {
                String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Main: creating the COP instance from " + path);
                System.out.println("---------------------------------------");
                }
                COP_Instance cop = Cerberus.getInstanceFromFile(path, false);
                //COP_Instance cop = Cerberus.getInstanceFromFile("/home/mik/NetBeansProjects/maxSum/problem.cop2",true);

                if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "original instance is: " + cop.toTestString());
                System.out.println("---------------------------------------");
                }*/


                boolean powerGridV = false;
                String filepath = path;
                boolean oldformatV = false;
                boolean screwupV = false;
                boolean boundedV = false;
                String solverV = "athena";
                String oplusV = "min";
                String otimesV = "sum";

                int iterationsV = 2500;

                boolean stepbystepV = false;
                boolean updateelV = false;

                String reportV = null;


                COP_Instance original_cop;
                if (powerGridV) {
                    PowerGrid pg = new PowerGrid(filepath);
                    //PowerGrid pg = new PowerGrid(2, 3, 2, 0.2, 0.1);
                    //original_cop = pg.getCopM();
                    original_cop = pg.getCopMnotInfNoCo2();
                    //System.out.println(pg.toStringFile());
                } else {
                    original_cop = Cerberus.getInstanceFromFile(filepath, oldformatV,100,10000);
                }

                //System.out.println(original_cop.toStringFile());

                COP_Instance cop;
                InstanceCloner ic = null;
                if (screwupV || boundedV) {
                    ic = new InstanceCloner(original_cop);
                    cop = ic.getClonedInstance();
                } else {
                    cop = original_cop;
                }


                ScrewerUp screwerup = null;

                if (screwupV) {
                    screwerup = new ScrewerUp(cop);
                    cop = screwerup.screwItUp();
                    System.out.println("Screwed istance:\n"+cop.toStringFile());
                }

                BoundedMaxSum BMax = null;
                if (boundedV) {
                    // time for BoundedMaxSum to do his best!
                    BMax = new BoundedMaxSum(cop.getFactorgraph());
                    //BMax.weightTheGraph();
                    BMax.letsBound();
                }

                Solver core = null;
                if (solverV.equalsIgnoreCase("athena")) {
                    core = new Athena(cop, oplusV, otimesV);
                } else if (solverV.equalsIgnoreCase("eris")) {
                    core = new Eris(oplusV, cop);
                }

                core.setIterationsNumber(iterationsV);

                core.setStepbystep(stepbystepV);
                core.setUpdateOnlyAtEnd(!stepbystepV);

                core.setUpdateOnlyAtEnd(!updateelV);

                if (reportV != null) {
                    core.pleaseReport(reportV);
                }

                core.solve();

                if (screwupV || boundedV) {
                    // set the variables value to the original instance
                    ic.setOriginalVariablesValues();
                }



                System.out.println("original value(cop)= " + cop.actualValue());














                //COP_Instance cop2 = new ClonedMSInstance((MS_COP_Instance) cop);



                /*
                InstanceCloner ic = new InstanceCloner(cop);
                COP_Instance cop2 = ic.getClonedInstance();


                System.out.println("Cloned instance value:\n"+cop2.toTestString());

                System.out.println("------------------------------------------------");
                 */

                /*
                Athena core = new Athena(cop, "max", "sum");
                core.setIterationsNumber(1000);
                core.setStepbystep(false);
                core.setUpdateOnlyAtEnd(true);
                core.solve_complete();
                System.exit(0);
                */

                /*
                Athena core2 = new Athena(cop2,"max", "sum");
                core2.setIterationsNumber(500);
                core2.setStepbystep(false);
                core2.solve_nIteration();
                core2.conclude();
                 */
                /*
                ClonedMSInstance cop3 = new ClonedMSInstance((MS_COP_Instance) cop);
                Athena core3 = new Athena(cop3);
                core3.setIterationsNumber(500);
                core3.setStepbystep(false);
                core3.solve_nIteration();
                core3.conclude();
                System.out.println("original value(cop3)= " + cop3.actualValue());
                 */


                //ic.setOriginalVariablesValues();
                //System.out.println("original value(ic)= " + ic.getActualOriginalValue());
                //System.out.println("original value(cop)= " + cop.actualValue());
                /*System.out.println("original value(cop2)= " + cop2.actualValue());

                ic.setOriginalVariablesValues();*/
                //System.out.println("cop new status: " + cop.status());

                //System.out.println("original renewed value(cop)= " + cop.actualValue());

                //System.out.println(ic.testString());





                /*if (cop.actualValue() == cop2.actualValue()){
                System.out.println("OK on "+path);
                }
                else {
                System.out.println("FAIL on "+path);
                }*/
            }
            /*
            ScrewerUp su = new ScrewerUp(cop);

            cop = su.screwItUp();

            if (debug>=3) {
            String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "screwed instance is: "+ cop.toTestString());
            System.out.println("---------------------------------------");
            }

            Athena core = new Athena(cop);

            core.setIterationsNumber(5);
            core.setStepbystep(false);
            String report = "reports/cop_";
            report += System.currentTimeMillis();
            report += ".report";
            core.pleaseReport(report);

            core.solve_nIteration();

            cop = su.fixItUp();

            core.conclude();


            if (debug>=3) {
            String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "fixed instance is: "+ cop.toTestString());
            System.out.println("---------------------------------------");
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Finish in " + ((System.currentTimeMillis() - startTime) / (double) 1000) + " s");
    }
    
    public ArrayList<SimpleEntry<String, String>> CalculateMaxSumAssignments(String inputCop)
    {
    	try {
    		BufferedReader br = new BufferedReader(new StringReader(inputCop));
			COP_Instance cop = Cerberus.getInstanceFromFile("", false,100,10000);
			Athena athena = new Athena(cop, "min", "sum"); 
			Solver core = athena;
            core.setIterationsNumber(2500);
            core.setStepbystep(false);
            core.setUpdateOnlyAtEnd(!false);
            core.setUpdateOnlyAtEnd(!false);
            core.solve();
            return athena.getCop().GetMaxValues();
            
		} catch (InvalidInputFileException | PostServiceNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
}

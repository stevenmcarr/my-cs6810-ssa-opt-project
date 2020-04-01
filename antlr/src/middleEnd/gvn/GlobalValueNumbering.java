package middleEnd.gvn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import middleEnd.iloc.IlocInstruction;
import middleEnd.iloc.Operand;
import middleEnd.iloc.PhiNode;
import middleEnd.iloc.SSAVROperand;
import middleEnd.iloc.VirtualRegisterOperand;
import middleEnd.utils.BasicBlock;
import middleEnd.utils.Cfg;
import middleEnd.utils.CfgNode;
import middleEnd.utils.IlocRoutine;
import middleEnd.utils.SSAOptimization;

public class GlobalValueNumbering extends SSAOptimization {

    private SSAGraph ssaGraph;
    LinkedList<StronglyConnectedComponent> sccs;
    Hashtable<String, SSAVROperand> valueTable = null;
    Hashtable<String, SSAVROperand> scratchTable = null;
    HashMap<String, SSAVROperand> valRep = null;

    public GlobalValueNumbering(String prevPassA, String passA) {
        super(prevPassA, passA);
    }

    @Override
    protected void performSSAOptimization() {
        for (IlocRoutine ir : getRoutines()) {
            buildSSAGraph(ir.getCfg());
            try {
                PrintWriter pw = new PrintWriter(inputFileName + "." + ir.getRoutineName() + ".ssag.dot");
                ssaGraph.emit(pw);
                pw.close();
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
            buildSCCs(ssaGraph);
            try {
                PrintWriter pw = new PrintWriter(inputFileName+"."+ir.getRoutineName()+".scc");
                for (StronglyConnectedComponent scc : sccs) {
                    pw.print(scc.getSCCId()+": ");
                    for (SSAGraphNode n : scc) {
                        pw.print(n.getSSAVR().toString()+", ");
                    }
                    pw.println("");
                }
                pw.close();
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
            valueTable = new Hashtable<String, SSAVROperand>();
            scratchTable = new Hashtable<String, SSAVROperand>();
            valRep = new HashMap<String, SSAVROperand>();
            for (String t : ssaGraph.defMap.keySet()) {
                valRep.put(t, null);
            }
            for (StronglyConnectedComponent scc : sccs) {
                if (scc.size() > 1) {
                    calcGlobalValueSCC(scc);
                    for (SSAGraphNode n : scc) {
                        SSAVROperand svr = n.getSSAVR();
                        IlocInstruction inst = ssaGraph.getSSAGraphNodeDef(svr.toString()).getInst();
                        SSAVROperand u = valRep.get(svr.toString());
                        String key = getHashKey(inst);
                        if (!valueTable.containsKey(key)) {
                            valueTable.put(key, u);
                        }
                    }
                } else {
                    IlocInstruction inst = ssaGraph.getNodes().getFirst().getInst();
                    if (inst instanceof PhiNode)
                        calcPhiValue((PhiNode) inst, valueTable);
                    else {
                        for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                            if (vr instanceof SSAVROperand) {
                                SSAVROperand t = (SSAVROperand) vr;
                                String key = getHashKey(inst);
                                if (!valueTable.containsKey(key)) {
                                    valRep.put(t.toString(), t);
                                    valueTable.put(key, t);
                                } else {
                                    valRep.put(key, valueTable.get(key));
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private SSAVROperand calcPhiValue(PhiNode p, Hashtable<String, SSAVROperand> valueTable) {
        String key = getHashKey(p);
        SSAVROperand newValRep = null;
        if (!valueTable.containsKey(key)) {
            newValRep = getPhiNodeValRep(p.getRValues());
            if (newValRep == null)
                newValRep = (SSAVROperand) p.getLValue();
            valueTable.put(key, newValRep);
        } else
            newValRep = valueTable.get(key);

        return newValRep;
    }

    private SSAVROperand getPhiNodeValRep(Vector<Operand> rValues) {
        SSAVROperand val = null;
        for (Operand op : rValues) {
            if (op instanceof SSAVROperand) {
                SSAVROperand svr = (SSAVROperand) op;
                if (val == null)
                    val = svr;
                else if (!svr.toString().equals(val.toString())) {
                    val = null;
                    break;
                }
            }
        }
        return val;
    }

    private String getHashKey(IlocInstruction inst) {
        String key = inst.getOpcode();
        for (Operand op : inst.getRValues()) {
            if (op instanceof SSAVROperand) {
                SSAVROperand svr = (SSAVROperand) op;
                SSAVROperand val = valRep.get(svr.toString());
                if (val == null)
                    key += "null";
                else
                    key += val.toString();
            } else
                key += op.toString();
        }
        return key;
    }

    private void calcGlobalValueSCC(StronglyConnectedComponent scc) {
        boolean change = false;
        do {
            SSAVROperand newValue = null;
            for (SSAGraphNode n : scc) {
                SSAVROperand t = n.getSSAVR();
                IlocInstruction i = ssaGraph.getSSAGraphNodeDef(t.toString()).getInst();
                if (i instanceof PhiNode) {
                    newValue = calcPhiValue((PhiNode) i, scratchTable);
                } else {
                    String key = getHashKey(i);
                    if (scratchTable.containsKey(key))
                        newValue = scratchTable.get(key);
                    else {
                        newValue = t;
                        scratchTable.put(key, t);
                    }
                }
                if (newValue.toString().equals(valRep.get(t.toString()).toString())) {
                    change = true;
                    valRep.put(t.toString(), newValue);
                }
            }
        } while (change);
    }

    private void buildSSAGraph(Cfg cfg) {
        ssaGraph = new SSAGraph();
        for (CfgNode n : cfg.getPreOrder()) {
            BasicBlock b = (BasicBlock) n;
            for (PhiNode p : b.getPhiNodes())
                buildSSAGraphNodes(p);
            Iterator<IlocInstruction> iter = b.iterator();
            while (iter.hasNext()) {
                IlocInstruction inst = iter.next();
                buildSSAGraphNodes(inst);
            }
        }
        for (CfgNode n : cfg.getPreOrder()) {
            BasicBlock b = (BasicBlock) n;
            for (PhiNode p : b.getPhiNodes())
                buildSSAGraph(p);
            Iterator<IlocInstruction> iter = b.iterator();
            while (iter.hasNext()) {
                IlocInstruction inst = iter.next();
                buildSSAGraph(inst);
            }
        }
    }

    private void buildSSAGraphNodes(IlocInstruction inst) {
        for (VirtualRegisterOperand vr : inst.getAllLValues()) {
            if (vr instanceof SSAVROperand) {
                SSAVROperand lval = (SSAVROperand) vr;
                SSAGraphNode lnode = ssaGraph.getSSAGraphNodeDef(lval.toString());
                if (lnode == null) {
                    lnode = (new SSAGraphNode()).addInst(inst).addSSAVR(lval);
                    ssaGraph.addNode(lnode);
                    ssaGraph.addDef(lval.toString(), lnode);
                }
            }
        }
    }

    private void buildSSAGraph(IlocInstruction inst) {
        for (Operand op : inst.getRValues()) {
            if (op instanceof SSAVROperand) {
                SSAVROperand rval = (SSAVROperand) op;
                SSAGraphNode rnode = (new SSAGraphNode()).addInst(inst).addSSAVR(rval);
                ssaGraph.addNode(rnode);
                SSAGraphNode lnode = ssaGraph.getSSAGraphNodeDef(rval.toString());
                lnode.addAdjacentNode(rnode);
                for (VirtualRegisterOperand vr : inst.getAllLValues()) {
                    if (vr instanceof SSAVROperand) {
                        SSAVROperand lval = (SSAVROperand) vr;
                        SSAGraphNode lnode2 = ssaGraph.getSSAGraphNodeDef(lval.toString());
                        rnode.addAdjacentNode(lnode2);
                    }
                }
            }
        }
    }

    // A recursive function that finds and prints strongly connected
    // components using DFS traversal
    // u --> The vertex to be visited next
    // disc[] --> Stores discovery times of visited vertices
    // low[] -- >> earliest visited vertex (the vertex with minimum
    // discovery time) that can be reached from subtree
    // rooted with current vertex
    // *st -- >> To store all the connected ancestors (could be part
    // of SCC)
    // stackMember[] --> bit/index array for faster check whether
    // a node is in stack
    private int time = 0;

    private void sccUtil(SSAGraphNode u, HashMap<String, Integer> disc, HashMap<String, Integer> low,
            LinkedList<SSAGraphNode> st, HashMap<String, Boolean> stackMember) {
        // A static variable is used for simplicity, we can avoid use
        // of static variable by passing a pointer.

        // Initialize discovery time and low value
        // disc[u.get] = low[u] = ++time;
        disc.put(u.toString(), ++time);
        low.put(u.toString(), time);
        st.push(u);
        // stackMember[u] = true;
        stackMember.put(u.toString(), true);

        // Go through all vertices adjacent to this
        // list<int>::iterator i;
        // for (i = adj[u].begin(); i != adj[u].end(); ++i)
        for (SSAGraphNode v : u.getAdjacentNodes()) {
            // int v = *i; // v is current adjacent of 'u'

            // If v is not visited yet, then recur for it
            // if (disc[v] == -1)
            if (disc.get(v.toString()) == -1) {
                sccUtil(v, disc, low, st, stackMember);

                // Check if the subtree rooted with 'v' has a
                // connection to one of the ancestors of 'u'
                // Case 1 (per above discussion on Disc and Low value)
                // low[u] = min(low[u], low[v]);
                low.put(u.toString(), Math.min(low.get(u.toString()), low.get(v.toString())));
            }

            // Update low value of 'u' only of 'v' is still in stack
            // (i.e. it's a back edge, not cross edge).
            // Case 2 (per above discussion on Disc and Low value)
            // else if (stackMember[v] == true)
            else if (stackMember.get(v.toString()))
                // low[u] = min(low[u], disc[v]);
                low.put(u.toString(), Math.min(low.get(u.toString()), disc.get(v.toString())));
        }

        // head node found, pop the stack and print an SCC
        // int w = 0; // To store stack extracted vertices
        SSAGraphNode w = null;
        StronglyConnectedComponent aSCC = new StronglyConnectedComponent();
        // if (low[u] == disc[u])
        if (low.get(u.toString()) == disc.get(u.toString())) {
            while (st.getFirst() != u) {
                // w = (int) st->top();
                w = st.getFirst();
                // cout << w << " ";
                aSCC.add(w);
                // stackMember[w] = false;
                stackMember.put(w.toString(), false);
                // st->pop();
                st.pop();
            }
            // w = (int) st->top();
            w = st.getFirst();
            // cout << w << "\n";
            aSCC.add(w);
            // stackMember[w] = false;
            stackMember.put(w.toString(), false);
            // st->pop();
            st.pop();
            sccs.add(aSCC);
        }
    }

    // The function to do DFS traversal. It uses SCCUtil()
    private void buildSCCs(SSAGraph g) {
        // int *disc = new int[V];
        HashMap<String, Integer> disc = new HashMap<String, Integer>();
        // int *low = new int[V];
        HashMap<String, Integer> low = new HashMap<String, Integer>();
        // bool *stackMember = new bool[V];
        HashMap<String, Boolean> stackMember = new HashMap<String, Boolean>();
        // stack<int> *st = new stack<int>();
        LinkedList<SSAGraphNode> st = new LinkedList<SSAGraphNode>();

        // Initialize disc and low, and stackMember arrays
        // for (int i = 0; i < V; i++)
        for (SSAGraphNode n : g.getNodes()) {
            // disc[i] = NIL;
            disc.put(n.toString(), -1);
            // low[i] = NIL;
            low.put(n.toString(), -1);
            // stackMember[i] = false;
            stackMember.put(n.toString(), false);
        }

        // Call the recursive helper function to find strongly
        // connected components in DFS tree with vertex 'i'
        // for (int i = 0; i < V; i++)
        sccs = new LinkedList<StronglyConnectedComponent>();
        g.computeDFSForest();
        for (DFSTree t : g.getForest())
            for (SSAGraphNode n : t) {
                // if (disc[i] == NIL)
                if (disc.get(n.toString()) == -1)
                    // SCCUtil(i, disc, low, st, stackMember);
                    sccUtil(n, disc, low, st, stackMember);
            }
    }
}
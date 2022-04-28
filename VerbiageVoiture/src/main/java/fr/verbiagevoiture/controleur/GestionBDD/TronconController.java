package fr.verbiagevoiture.controleur.GestionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TronconController{
    public Connection conn;
    
    public TronconController(Connection c) {
    	conn = c;
    }
    // requetes spécifiques
    //return troncon which are booking by the user
    public ArrayList<int []> getTronconEmprunte(String email) {
    	//query creation
    	ArrayList<int []> myTrajet = new ArrayList<int []>();
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = conn.prepareStatement("SELECT IDTRAJET, NUMERO_TRONCON FROM EMPRUNTE WHERE EMAIL = ?");
    		pstmt.setString(1, email);
    	} catch (SQLException e1) {
    	    System.err.println("failed to create new prepareStatement (getTronconEmprunte)");
    		e1.printStackTrace();
    	}
    	
    	//query execution
    	ResultSet rset = null;
    	try {
    	    rset =  pstmt.executeQuery();
    	} catch (SQLException e) {
    	    System.err.println("failed to executeQuery (getMyTrajet)");
    		e.printStackTrace();
    	}
    	int i=0;
    	//response analysis
    	try {
        	while(rset.next()) {
        		myTrajet.add(new int[2]);
        		myTrajet.get(i)[0]= rset.getInt(1);
        		myTrajet.get(i)[1]= rset.getInt(2);
        		i++;
        	}
    	}  catch (SQLException e) {
            System.err.println("failed for the access to  ResultSet (getMyTrajet)");
            e.printStackTrace(System.err);
        }
    	//close
    	try {
    		rset.close();
    	} catch (SQLException e) {
    	    System.err.println("failed to close (getMyTrajet)");
    		e.printStackTrace();
    	}
    	return myTrajet;
    }
    
    public boolean deleteEmprunte(int idTrajet) {
    	boolean b = false;
    	//query creation
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = conn.prepareStatement("DELETE FROM EMPRUNTE WHERE IDTRAJET = ?");
    		pstmt.setInt(1, idTrajet);
    	} catch (SQLException e1) {
    	    System.err.println("failed to create new prepareStatement (deleteEmprunte)");
    		e1.printStackTrace();
    	}
    	//query execution
    	int rset=0;
    	try {
    	    rset =  pstmt.executeUpdate();
    	} catch (SQLException e) {
    	    System.err.println("failed to executeQuery (deleteEmprunte)");
    		e.printStackTrace();
    	}
    	//response analysis
    	b = rset>0; //if at least one line was deleted, rset>0
    	
    	//return 
    	return b;
    }

    public boolean deleteEmprunte(int numTroncon, int idTrajet) {
    	boolean b = false;
    	//query creation
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = conn.prepareStatement("DELETE FROM EMPRUNTE WHERE IDTRAJET = ? AND NUMERO_TRONCON = ?");
    		pstmt.setInt(1, idTrajet);
    		pstmt.setInt(2, numTroncon);
    	} catch (SQLException e1) {
    	    System.err.println("failed to create new prepareStatement (deleteEmprunte)");
    		e1.printStackTrace();
    	}
    	//query execution
    	int rset=0;
    	try {
    	    rset =  pstmt.executeUpdate();
    	} catch (SQLException e) {
    	    System.err.println("failed to executeQuery (deleteEmprunte)");
    		e.printStackTrace();
    	}
    	//response analysis
    	b = rset>0; //if at least one line was deleted, rset>0
    	
    	//return 
    	return b;
    }
    
    public boolean addEmprunte(int numTroncon, int idTrajet, String email) {
    	boolean b = false;
    	//query creation
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = conn.prepareStatement("INSERT INTO EMPRUNTE VALUES (?, ?, ?)");
			pstmt.setString(1, email);
			pstmt.setInt(2, numTroncon);
			pstmt.setInt(3, idTrajet);
    	} catch (SQLException e1) {
    	    System.err.println("failed to create new prepareStatement (addEmprunte)");
    		e1.printStackTrace();
    	}
    	//query execution
    	int rset=0;
    	try {
    	    rset =  pstmt.executeUpdate();
    	} catch (SQLException e) {
    	    System.err.println("failed to executeQuery (addEmprunte)");
    		e.printStackTrace();
    	}
    	//response analysis
    	b = rset==1;
    	
    	//return 
    	return b;
    }
    
    public int getNumberTroncon(int idTrajet) {
    	//query creation
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = conn.prepareStatement("SELECT COUNT(*)  FROM TRONCON WHERE IDTRAJET = ?");
    		pstmt.setInt(1, idTrajet);
    	} catch (SQLException e1) {
    	    System.err.println("failed to create new prepareStatement (getNumberTroncon)");
    		e1.printStackTrace();
    	}
    	
    	//query execution
    	ResultSet rset = null;
    	try {
    	    rset =  pstmt.executeQuery();
    	} catch (SQLException e) {
    	    System.err.println("failed to executeQuery (getNumberTroncon)");
    		e.printStackTrace();
    	}
    	int value = 0;
    	//response analysis
    	try {
    		while(rset.next()) {
        		value = rset.getInt(1);
    		}
    	}  catch (SQLException e) {
            System.err.println("failed for the access to  ResultSet (getMyTrajet)");
            e.printStackTrace(System.err);
        }
    	//close
    	try {
    		rset.close();
    	} catch (SQLException e) {
    	    System.err.println("failed to close (getMyTrajet)");
    		e.printStackTrace();
    	}
    	return value;
    }
    
    public float coutTroncon(int numTroncon, int idTrajet ) {
    	float prix = 0;
    	
    	//query creation
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = conn.prepareStatement("SELECT E.COUT_ENERGIE, V.PUISSANCE_FISCALE, TRO.DISTANCE_PARCOURUE"
    										+" FROM TRAJET TRA, TRONCON TRO, VEHICULE V, ENERGIE_PRIX E"
    										+" WHERE TRO.IDTRAJET = TRA.IDTRAJET AND TRA.IMMATRICULATION = V.IMMATRICULATION AND V.ENERGIE_UTILISE = E.ENERGIE_UTILISE"
    										+" AND TRO.NUMERO_TRONCON = ? AND TRO.IDTRAJET = ?");
    		pstmt.setInt(1, numTroncon);
    		pstmt.setInt(2, idTrajet);
    	} catch (SQLException e1) {
    	    System.err.println("failed to create new prepareStatement (coutTroncon)");
    		e1.printStackTrace();
    	}
    	
    	//query execution
    	ResultSet rset = null;
    	try {
    	    rset =  pstmt.executeQuery();
    	} catch (SQLException e) {
    	    System.err.println("failed to executeQuery (coutTroncon)");
    		e.printStackTrace();
    	}
    	float[] value = new float[3]; //value == [coutNRJ, Pfiscale, distance]
    	//response analysis
    	try {
    		while(rset.next()) {
        		value[0] = rset.getFloat(1);
        		value[1] = rset.getInt(2);
        		value[1] = rset.getInt(3);
    		}
    	}  catch (SQLException e) {
            System.err.println("failed for the access to  ResultSet (coutTroncon)");
            e.printStackTrace(System.err);
        }
    	//close
    	try {
    		rset.close();
    	} catch (SQLException e) {
    	    System.err.println("failed to close (coutTroncon)");
    		e.printStackTrace();
    	}
    	
    	prix = (float) (value[0]*value[1]*0.1*value[2]);
    	
    	return prix;
    }
}

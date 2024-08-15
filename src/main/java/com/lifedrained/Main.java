package com.lifedrained;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
       String dbUrl = "jdbc:derby:drained_db;create=true";
       String tableName = "TESTTAB";
       String blobTableName = "BLOBS";
        Statement stmt ;
        try(Connection connection = DriverManager.getConnection(dbUrl)){
            stmt = connection.createStatement();
            String blobTabCreate = "CREATE TABLE  "+blobTableName+" ( id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY ," +
                    " blob BLOB) ";
    try {
    stmt.execute(blobTabCreate);
    }catch (SQLException e){
    System.out.println("Table exists");
    }
            Scanner in = new Scanner(System.in);
    System.out.println("Wanna blob?");
    String answer = in.nextLine();
            String name = null,pos=null;
    if(answer.equalsIgnoreCase("yes")){
        while (true){
            System.out.println("Enter data");
            name = in.nextLine();
            pos = in.nextLine();
            if(!pos.isEmpty()&&!name.isEmpty()) {
                BlobObj blob = new BlobObj(name,pos);

                String query = "INSERT INTO " + blobTableName + "(blob) VALUES " +
                        "(?)";
                PreparedStatement pstmt = connection.prepareStatement(query);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(blob);
                oos.flush();
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                pstmt.setBlob(1, bais);
                pstmt.execute();
                bais.close();
                oos.close();
                baos.close();
            }else{
                break;
            }
        }
    }else if(answer.equals("no")) {
        while (true){
            System.out.println("Enter data");
            name = in.nextLine();
            pos = in.nextLine();
            if(!pos.isEmpty()&&!name.isEmpty()) {



                String query = "INSERT INTO " + tableName + "(name, pos) VALUES " +
                        "('" + name + "','" + pos + "')";
                stmt.execute(query);

            }else{
                break;
            }
        }
    }


            stmt.close();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName+" WHERE name ='pos' ");
            while(rs.next()){
                System.out.println(rs.getString(2)+":"+rs.getString(3));
            }
            System.out.println("Primitive data done");
            Statement stmt2 = connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM " + blobTableName );
            while(rs2.next()){
                Blob blob = rs2.getBlob(2);
                ByteArrayInputStream bais =  new ByteArrayInputStream(blob.getBytes(1,(int)blob.length()));
                ObjectInputStream ois = new ObjectInputStream(bais);
                BlobObj obj = (BlobObj) ois.readObject();
                System.out.println(obj.getName()+":"+obj.getPos());
            }
            System.out.println("Blob data done");
        }catch (SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
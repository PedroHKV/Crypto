package com.geheimnis.auxiliares;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Arquivista {
    
    private FileInputStream Fin;
    private FileOutputStream Fout;

    // le até o fim do arquivo ou o buffer estar completo
    public int read(byte[] buffer) throws IOException{
        int total = 0;
        while (total < buffer.length) {
            int r = Fin.read(buffer, total, buffer.length - total);
            if (r == -1) 
                return total == 0 ? -1 : total;
            total += r;
        }
        return total;
    }

    public void write(byte[] buffer) throws IOException{
        Fout.write(buffer);
    }

    public void close() throws IOException{
        Fin.close();
        Fout.close();
    }

    public void setFin(File file) {
        try{ 
            Fin = new FileInputStream(file);
        } catch (Exception e) { e.printStackTrace(); } 
    }

    public void setFout(File file, boolean append) {
        try{
            Fout = new FileOutputStream(file, append);
        } catch (Exception e){ e.printStackTrace(); }
    }

}

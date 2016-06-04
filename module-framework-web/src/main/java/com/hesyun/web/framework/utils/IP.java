package com.hesyun.web.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IP {
	private static final Log logger =LogFactory.getLog(IP.class);


    public static boolean enableFileWatch = false;

    private static int offset;
    private static int[] index = new int[256];
    private static ByteBuffer dataBuffer;
    private static ByteBuffer indexBuffer;
    private static InputStream ipFileStream ;
    private static ReentrantLock lock = new ReentrantLock();

    static{
    	logger.debug("loading ip database file:  ipdb.dat");
    	ipFileStream = IP.class.getClassLoader().getResourceAsStream("ipdb.dat");
    	IP.load();
    }
    public static String[] find(String ip) {
    	
        int ip_prefix_value = new Integer(ip.substring(0, ip.indexOf(".")));
        long ip2long_value  = ip2long(ip);
        int start = index[ip_prefix_value];
        int max_comp_len = offset - 1028;
        long index_offset = -1;
        int index_length = -1;
        byte b = 0;
        for (start = start * 8 + 1024; start < max_comp_len; start += 8) {
            if (int2long(indexBuffer.getInt(start)) >= ip2long_value) {
                index_offset = bytesToLong(b, indexBuffer.get(start + 6), indexBuffer.get(start + 5), indexBuffer.get(start + 4));
                index_length = 0xFF & indexBuffer.get(start + 7);
                break;
            }
        }

        byte[] areaBytes;
        lock.lock();
        try {
            dataBuffer.position(offset + (int) index_offset - 1024);
            areaBytes = new byte[index_length];
            dataBuffer.get(areaBytes, 0, index_length);
        } finally {
            lock.unlock();
        }

        return new String(areaBytes).split("\t");
    }

    private static void load() {
        InputStream fin = ipFileStream;
        lock.lock();

        try {
//            dataBuffer = ByteBuffer.allocate(Long.valueOf(ipFile.length()).intValue());
//            int readBytesLength;
//            byte[] chunk = new byte[4096];
//            while (fin.available() > 0) {
//                readBytesLength = fin.read(chunk);
//                dataBuffer.put(chunk, 0, readBytesLength);
//            }
        	logger.debug("start to read data to baos");
        	int BUFSIZE = 1024;
        	ByteArrayOutputStream out = new ByteArrayOutputStream(BUFSIZE);
        	 
        	byte[] tmp = new byte[BUFSIZE];
        	 
        	while (true) {
        	    int r = fin.read(tmp);
        	    if (r == -1) break;
        	    out.write(tmp,0,r);
        	}
        	out.flush();
        	out.close();
        	dataBuffer = ByteBuffer.wrap(out.toByteArray());
        	
            dataBuffer.position(0);
            int indexLength = dataBuffer.getInt();
            byte[] indexBytes = new byte[indexLength];
            dataBuffer.get(indexBytes, 0, indexLength - 4);
            indexBuffer = ByteBuffer.wrap(indexBytes);
            indexBuffer.order(ByteOrder.LITTLE_ENDIAN);
            offset = indexLength;
            int loop = 0;
            while (loop++ < 256) {
                index[loop - 1] = indexBuffer.getInt();
            }
            indexBuffer.order(ByteOrder.BIG_ENDIAN);
        } catch (IOException ioe) {

        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e){}
            lock.unlock();
        }
        logger.debug("load ip db file successfully");
    }

    private static long bytesToLong(byte a, byte b, byte c, byte d) {
        return int2long((((a & 0xff) << 24) | ((b & 0xff) << 16) | ((c & 0xff) << 8) | (d & 0xff)));
    }

    private static int str2Ip(String ip)  {
        String[] ss = ip.split("\\.");
        int a, b, c, d;
        a = Integer.parseInt(ss[0]);
        b = Integer.parseInt(ss[1]);
        c = Integer.parseInt(ss[2]);
        d = Integer.parseInt(ss[3]);
        return (a << 24) | (b << 16) | (c << 8) | d;
    }

    private static long ip2long(String ip)  {
        return int2long(str2Ip(ip));
    }

    private static long int2long(int i) {
        long l = i & 0x7fffffffL;
        if (i < 0) {
            l |= 0x080000000L;
        }
        return l;
    }
}
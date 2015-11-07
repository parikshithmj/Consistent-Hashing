import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class RedisConnect {
	public static void main(String args[]){
		RedisConnect rc=new RedisConnect();
		String host, results = rc.readFromCSV();
		int port;
		String[] hostsAndPorts = results.split("\n");
		Set<HostAndPort> jedisClusterNodes = null;
		for(String hostAndPort:hostsAndPorts){
		 jedisClusterNodes = new HashSet<HostAndPort>();
		//Jedis Cluster will attempt to discover cluster nodes automatically
		host =hostAndPort.split(",")[0];
		//System.out.println("**********"+host);
		port =Integer.parseInt(hostAndPort.split(",")[1]);
		jedisClusterNodes.add(new HostAndPort(host, port));
		
		}
		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		for(int i=0;i<100;i++){
		jc.set("thisisthekey"+i, "bar");
		String value = jc.get("foo"+i);
		i++;
		System.out.println("value is "+value);
		}
	}
	
	
	public String readFromCSV(){
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("redis-server-info.csv").getFile());

		try {
		Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			
			scanner.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}

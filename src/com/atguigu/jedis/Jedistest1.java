package com.atguigu.jedis;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class Jedistest1 {
	
	@Test
	public void test1() {
		Jedis jedis = new Jedis("192.168.0.103", 6379);
		String string = jedis.set("liner", "love");
		String liner = jedis.get("liner");
		System.out.println(liner);
	}
	
	//连接池
	@Test
	public void test2() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(1000);
		poolConfig.setMaxIdle(32);
		poolConfig.setMaxWait(100 * 1000);
		JedisPool jedisPool = new JedisPool(poolConfig, "192.168.0.103", 6379);
		Jedis jedis = jedisPool.getResource();
		String liner = jedis.get("liner");
		System.out.println(liner);
		jedisPool.returnBrokenResource(jedis);
		jedisPool.destroy();
		
	}
	
	@Test
	public void test3() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(1000);
		poolConfig.setMaxIdle(32);
		poolConfig.setMaxWait(100 * 1000);
		
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.0.103", 6379));
		ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, shards);
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			String liner = shardedJedis.get("liner");
			System.out.println(liner);
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if(shardedJedis != null) {
				shardedJedisPool.returnResource(shardedJedis);
			}
 		}
		shardedJedisPool.destroy();
	}
	
}

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Builds an inverted index: each word followed by files it was found in.
 *
 *
 */
public class InvertedIndex
{

	public static class InvertedIndexMapper extends
			Mapper<LongWritable, Text, Text, Text>
	{

		private final static Text word = new Text();
		private final static Text location = new Text();
		//private final static MapWritable myMap = new MapWritable(); // <file, count>

		public void map(LongWritable key, Text val, Context context)
				throws IOException, InterruptedException
		{

			/* Split the files between datanodes*/
			FileSplit fileSplit = (FileSplit) context.getInputSplit();
			String fileName = fileSplit.getPath().getName();
			location.set(fileName);

			//myMap.put(new IntWritable(1), new Text(...));

			/* Grab the whole text file in a line and loop through each word in the file:
 			 * emit the word as the key and file location as the value */
			String line = val.toString();
			StringTokenizer itr = new StringTokenizer(line.toLowerCase(),
					" , .;:\"&!?-_\n\t12345678910[]{}<>\\`~|=^()@#$%^*/+-");
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				//String tmp = itr.nextToken();
				//word.set(tmp);
				//If myMap.containsValue(itr.nextToken);
				//
				context.write(word, location);
			}
		}
	}

	public static class InvertedIndexReducer extends
			Reducer<Text, Text, Text, Text>
	{

		//private final static MapWritable myMap = new MapWritable();
		//private final static HashMap<Text,Text> myMap = new HashMap<Text,Text>(); //<file, count>

		public void reduce(Text key, Iterable<Text> values, Context context) //<key_in, value_in, key_out, value_out>
				throws IOException, InterruptedException
		{

			//private final static MapWritable myMap = new MapWritable();
			HashMap<String,Integer> myMap = new HashMap<String,Integer>(); //<file, count>

			//myMap.put(new IntWritable(1), new Text(...));

			/* Loop through each file that the word has appeared */
			boolean first = true;
			Iterator<Text> itr = values.iterator();
			int count = 0;
			String filename = "";
			StringBuilder toReturn = new StringBuilder();
			while (itr.hasNext()) {
				//if (!first)
				//	toReturn.append(", ");
				//first = false;
				//toReturn.append(itr.next().toString());
				filename = itr.next().toString();
				count++;

				if (myMap.containsKey(filename)) {
					myMap.replace(filename, count);
				}
				else {
					myMap.put(filename, count);
				}
			}

			Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

			sortedMap = sortMap(myMap);



			//iterate over the hashmap
			//Make string out of keys and values
			for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
				toReturn.append(entry.getValue());
				toReturn.append(" ");
				toReturn.append(entry.getKey());
				toReturn.append(", ");
    		}

			//toReturn.append(String.valueOf(count));
			//toReturn.append(" ");
			//toReturn.append(filename);
			//toReturn.append(", ");
			//myMap.put(new Text(String.valueOf(count)), new);

			context.write(key, new Text(toReturn.toString()));
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException
	{
		Configuration conf = new Configuration();
		if (args.length < 2) {
			System.out
					.println("Usage: InvertedIndex <input path> <output path>");
			System.exit(1);
		}
		Job job = new Job(conf, "InvertedIndex");
		job.setJarByClass(InvertedIndex.class);
		job.setMapperClass(InvertedIndexMapper.class);
		job.setReducerClass(InvertedIndexReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	public static HashMap<String, Integer> sortMap(HashMap<String, Integer> map){
		HashMap<String, Integer> sortedMap = new LinkedHashMap<String,Integer>();
	//	ArrayList<String> keys = new ArrayList<>(map.keySet());
	//	ArrayList<String> values = new ArrayList<>(map.values());

	//	Collections.sort(values);
	//	Collections.sort(keys);

		Set<Entry<String, Integer>> mapEntries = map.entrySet();

		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(mapEntries);

		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
				return obj2.getValue().compareTo(obj1.getValue());
			}

		});

		for(Entry<String,Integer> entry : list){
			sortedMap.put(entry.getKey(), entry.getValue());
		}
/*


			Iterator<String> valitr = values.iterator();
			while (valitr.hasNext()){
				String val = valitr.next();

				Iterator<String> keyitr = keys.iterator();

				while (keyitr.hasNext()){
					String key = keyitr.next();
					String temp1 = map.get(key);
					String temp2 = val;

					if(temp1.equals(temp2)){
						keyitr.remove();
						sortedMap.put(key, val);
						break;
					}
				}
			} */
		return sortedMap;
	}

}


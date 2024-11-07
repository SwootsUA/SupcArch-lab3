package main;

import java.util.List;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.node.protocol.Task;

public class FactorialClient {
	public static void main(String[] args) {
		try (JPPFClient jppfClient = new JPPFClient()) {
			JPPFJob job = new JPPFJob();
			job.setName("Factorial job");
			job.setBlocking(true);
			
			for (int i = 50000; i <= 50020; ++i) {
				job.add(new FactorialTask(i)).setId("Factorial task (" + i + "!)");
			}
			
			List<Task<?>> results = jppfClient.submitJob(job);
			
			for (Task<?> task: results) {
				if (task.getThrowable() != null) {
					System.out.println(task.getId() + ", an exception was raised: " + task.getThrowable().getMessage());
				} else {
					System.out.println(task.getId() + ", result digit count: " + task.getResult());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
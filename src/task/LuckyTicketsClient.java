package task;

import java.util.List;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.node.protocol.Task;

public class LuckyTicketsClient {
	public static void main(String[] args) {
		try (JPPFClient jppfClient = new JPPFClient()) {
			JPPFJob job = new JPPFJob();
			job.setName("Lucky tickets job");
			job.setBlocking(true);
			
			for (int i = 1; i <= 10; ++i) {
				job.add(new LuckyTicketsTask(i, false)).setId("Lucky tickets task (" + i + ")");
			}
			
			List<Task<?>> results = jppfClient.submitJob(job);
			
			for (Task<?> task: results) {
				if (task.getThrowable() != null) {
					System.out.println(task.getId() + ", an exception was raised: " + task.getThrowable().getMessage());
				} else {
					System.out.println(task.getId() + ", result lucky tickets count: " + task.getResult());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
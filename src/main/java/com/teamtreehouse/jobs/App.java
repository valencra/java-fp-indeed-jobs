package com.teamtreehouse.jobs;

import com.teamtreehouse.jobs.model.Job;
import com.teamtreehouse.jobs.service.JobService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class App {

  public static void main(String[] args) {
    JobService service = new JobService();
    boolean shouldRefresh = false;
    try {
      if (shouldRefresh) {
        service.refresh();
      }
      List<Job> jobs = service.loadJobs();
      System.out.printf("Total jobs:  %d %n %n", jobs.size());
      explore(jobs);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void explore(List<Job> jobs) {
    // Portland, OR jobs
    jobs.stream()
        .filter(job -> job.getState().equals("OR"))
        .filter(job -> job.getCity().equals("Portland"))
        .forEach(System.out::println);

    // 3 junior jobs
    getThreeJuniorJobsStream(jobs).forEach(System.out::println);
  }

  private static List<Job> getThreeJuniorJobsStream(List<Job> jobs) {
    return jobs.stream()
        .filter(App::isJuniorJob)
        .limit(3)
        .collect(Collectors.toList());
  }

  private static boolean isJuniorJob(Job job) {
    String title = job.getTitle().toLowerCase();
    return title.contains("junior") || title.contains("jr");
  }
}

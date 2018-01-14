package com.teamtreehouse.jobs;

import com.teamtreehouse.jobs.model.Job;
import com.teamtreehouse.jobs.service.JobService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    // 3 captions
    getCaptionsStream(jobs).forEach(System.out::println);

    // get snippet words counts
    getSnippetWordCountsStream(jobs).forEach((word, count) -> System.out.printf("%s has %d instances%n", word, count));

    System.out.println(
        jobs.stream()
            .map(Job::getCompany)
            .max(Comparator.comparingInt(String::length))
    );

  }

  private static List<Job> getThreeJuniorJobsStream(List<Job> jobs) {
    return jobs.stream()
        .filter(App::isJuniorJob)
        .limit(3)
        .collect(Collectors.toList());
  }

  private static List<String> getCaptionsStream(List<Job> jobs) {
    return jobs.stream()
        .filter(App::isJuniorJob)
        .map(job -> String.format("%s is looking for a %s in %s", job.getCompany(), job.getTitle(), job.getCity()))
        .limit(3)
        .collect(Collectors.toList());
  }

  private static boolean isJuniorJob(Job job) {
    String title = job.getTitle().toLowerCase();
    return title.contains("junior") || title.contains("jr");
  }

  public static Map<String, Long> getSnippetWordCountsStream(List<Job> jobs) {
    return jobs.stream()
        .map(Job::getSnippet)
        .map(snippet -> snippet.split("\\W+"))
        .flatMap(Stream::of)
        .filter(word -> word.length() > 0)
        .map(String::toLowerCase)
        .collect(Collectors.groupingBy(
            Function.identity(),
            Collectors.counting()
        ));
  }
}

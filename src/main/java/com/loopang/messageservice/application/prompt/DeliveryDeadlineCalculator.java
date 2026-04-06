package com.loopang.messageservice.application.prompt;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DeliveryDeadlineCalculator {

  private static final int WORK_START_HOUR = 9;
  private static final int WORK_END_HOUR = 18;

  public String calculateDeadline(LocalDateTime orderTime, List<Integer> expectedTimes) {
    LocalDateTime deadline = calculateDeadlineDateTime(orderTime, expectedTimes);
    return formatDeadline(deadline);
  }

  public LocalDateTime calculateDeadlineDateTime(LocalDateTime orderTime, List<Integer> expectedTimes) {
    LocalDateTime adjustedStart = adjustToWorkingTime(orderTime);
    LocalDateTime currentTime = adjustedStart;

    if (expectedTimes == null || expectedTimes.isEmpty()) {
      return currentTime;
    }

    for (Integer expectedTime : expectedTimes) {
      if (expectedTime == null || expectedTime <= 0) {
        continue;
      }

      currentTime = moveSegment(currentTime, expectedTime);
    }

    return currentTime;
  }

  private LocalDateTime adjustToWorkingTime(LocalDateTime time) {
    if (time.getHour() < WORK_START_HOUR) {
      return time.withHour(WORK_START_HOUR)
          .withMinute(0)
          .withSecond(0)
          .withNano(0);
    }

    if (time.getHour() >= WORK_END_HOUR) {
      return nextWorkingStart(time);
    }

    return time;
  }

  private LocalDateTime moveSegment(LocalDateTime startTime, int expectedTime) {
    LocalDateTime departureTime = adjustToWorkingTime(startTime);

    while (true) {
      LocalDateTime workEndTime = departureTime.withHour(WORK_END_HOUR)
          .withMinute(0)
          .withSecond(0)
          .withNano(0);

      long availableToday = Duration.between(departureTime, workEndTime).toMinutes();

      if (availableToday <= 0) {
        departureTime = nextWorkingStart(departureTime);
        continue;
      }

      if (expectedTime <= availableToday) {
        return departureTime.plusMinutes(expectedTime);
      }

      departureTime = nextWorkingStart(departureTime);
    }
  }

  private LocalDateTime nextWorkingStart(LocalDateTime time) {
    return time.plusDays(1)
        .withHour(WORK_START_HOUR)
        .withMinute(0)
        .withSecond(0)
        .withNano(0);
  }

  private String formatDeadline(LocalDateTime time) {
    String amPm = time.getHour() < 12 ? "오전" : "오후";

    int hour = time.getHour() % 12;
    if (hour == 0) {
      hour = 12;
    }

    return "%d-%02d-%02d %s %d시 %02d분".formatted(
        time.getYear(),
        time.getMonthValue(),
        time.getDayOfMonth(),
        amPm,
        hour,
        time.getMinute()
    );
  }
}
package ourtine.domain;

import ourtine.domain.enums.HabitStatus;
import javax.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Private")
public class PrivateHabit extends Habit {
    @Builder
    public PrivateHabit(User host, String title, String detail, String imageUrl, Long categoryId, Long followerLimit,
                        LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate){
        super(host, title, detail, imageUrl, categoryId, followerLimit, startTime, endTime, startDate, endDate, HabitStatus.PRIVATE);

    }

}

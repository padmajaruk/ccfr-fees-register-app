package uk.gov.hmcts.fees.register.api.model;

import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import uk.gov.hmcts.fees.register.api.model.exceptions.RangeNotFoundException;

import static com.google.common.base.MoreObjects.firstNonNull;

@Data
@Entity
@NoArgsConstructor
public class RangeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String code;

    @NonNull
    private String description;

    @OneToMany(mappedBy = "rangeGroup", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    private List<Range> ranges;

    @Builder(builderMethodName = "rangeGroupWith")
    public RangeGroup(Integer id, String code, String description, List<Range> ranges) {
        this.id = id;
        this.code = code;
        this.description = description;
        setRanges(ranges);
    }

    public void setRanges(List<Range> ranges) {
        checkRangeIsContinuous(ranges);
        this.ranges = firstNonNull(this.ranges, new ArrayList<>());
        this.ranges.clear();
        this.ranges.addAll(ranges);
        this.ranges.forEach(range -> range.setRangeGroup(this));
    }

    private void checkRangeIsContinuous(List<Range> ranges) {
        List<Range> sortedRanges = Ordering.from(Comparator.comparing(Range::getFrom)).sortedCopy(ranges);
        for (int i = 0; i < sortedRanges.size() - 1; i++) {
            if (!sortedRanges.get(i + 1).follows(sortedRanges.get(i))) {
                throw new RangeGroupNotContinuousException();
            }
        }
    }

    public Fee findFeeForValue(int value) {
        Optional<Range> first = ranges.stream().filter(range -> range.containsValue(value)).findFirst();
        Range range = first.orElseThrow(() -> new RangeNotFoundException(value));
        return range.getFee();
    }

    public int findMaxRangeValue() {
        return ranges.stream().map(range -> range.getTo() != null ? range.getTo() :range.getFrom()).max(Integer::compare).get();
    }
}

wpackage com.valhallagame.statisticsserviceserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valhallagame.statisticsserviceserver.model.Statistics;

public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {
	public List<Statistics> findByCharacterOwner(String characterOwner);
}

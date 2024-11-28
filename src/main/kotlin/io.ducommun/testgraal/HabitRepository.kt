package io.ducommun.testgraal

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HabitRepository : ReactiveCrudRepository<Habit, Int>

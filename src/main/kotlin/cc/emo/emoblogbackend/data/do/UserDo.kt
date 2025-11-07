package cc.emo.emoblogbackend.data.`do`

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "USERS",
    uniqueConstraints = [UniqueConstraint(columnNames = ["username"])]
)
class UserDo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var username: String,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(nullable = false)
    var passwordHash: String,

    @Column
    var birthday: LocalDate,
)

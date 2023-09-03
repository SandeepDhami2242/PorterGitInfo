import org.kohsuke.github.*
import java.text.SimpleDateFormat
import java.util.*

fun main() {
  // Access token for GitHub API authentication
  val accessToken = "ghp_eGYP89uwtVhNNqDna4znz4JN12pTIS2G2cf8"
  val github = GitHubBuilder().withOAuthToken(accessToken).build()

  // Configuration details
  val repositories = listOf("customerApp", "customerapp-shared", "kmp-utils")
  val users = listOf("appworkd", "nimisha719", "SandeepDhami2242")
  val sdf = SimpleDateFormat("yyyy-MM-dd")
  val startDate = sdf.parse("2023-08-01") ?: return
  val endDate = sdf.parse("2023-08-31") ?: return

  var totalCycleTime = 0.0
  var totalPRCount = 0
  val branches = mutableSetOf<String>()

  // Calculate PR cycle time for each repository
  for (repoName in repositories) {
    val repository = github.getRepository("porterin/$repoName")
    println(repository.name)

    var totalRepoCycleTime = 0.0
    var prCount = 0

    val pullRequests = repository.queryPullRequests()
      .state(GHIssueState.CLOSED)
      .sort(GHPullRequestQueryBuilder.Sort.CREATED)
      .direction(GHDirection.DESC)
      .list()

    for (pullRequest in pullRequests) {

      if (pullRequest.mergedAt == null) {
        continue
      }

      if (pullRequest.mergedAt.before(startDate)) {
        break
      }

      if (isValidPullRequest(
          pullRequest,
          users,
          startDate,
          endDate,
          branches
        )
      ) {
        val branchName = pullRequest.head.label
        branches.add(branchName)
        val cycleTime = calculateCycleTime(pullRequest)
        totalRepoCycleTime += cycleTime
        prCount++
      }
      print("#")
    }

    println("")
    printRepoStats(repoName, prCount, totalRepoCycleTime)
    if (prCount > 0) {
      totalCycleTime += totalRepoCycleTime
      totalPRCount += prCount
    }
  }
  println(branches.size)

  printNetCycleTime(totalPRCount, totalCycleTime)
}

fun isValidPullRequest(
  pullRequest: GHPullRequest,
  users: List<String>,
  startDate: Date,
  endDate: Date,
  branches: MutableSet<String>
): Boolean {
  val branchName = pullRequest.head.label
  return pullRequest.user.login in users &&
    pullRequest.mergedAt in startDate..endDate &&
    branchName.contains(CUSTOMER_APP_BRANCH_SUFFIX) &&
    !branches.contains(branchName)
}

fun calculateCycleTime(pullRequest: GHPullRequest): Double {
  val firstCommitTime =
    pullRequest.listCommits().iterator().next().commit.author.date
  return (pullRequest.mergedAt.time - firstCommitTime.time) / 3600000.0  // In hours
}

fun printRepoStats(repoName: String, prCount: Int, totalRepoCycleTime: Double) {
  val averageRepoCycleTime = totalRepoCycleTime / prCount
  println("Average PR cycle time for $prCount PRs in $repoName: ${averageRepoCycleTime.format()} hours (${(averageRepoCycleTime / 24).format()} days)")
}

fun printNetCycleTime(totalPRCount: Int, totalCycleTime: Double) {
  val netCycleTime = totalCycleTime / totalPRCount
  println("Net PR cycle time for $totalPRCount PRs: ${netCycleTime.format()} hours (${(netCycleTime / 24).format()} days)")
}

fun Double.format() = String.format("%.2f", this)

const val CUSTOMER_APP_BRANCH_SUFFIX = "/master"


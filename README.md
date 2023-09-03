# GitHub PR Cycle Time Calculator

This application calculates the average Pull Request (PR) cycle time for a list of specified repositories.

## Overview

The application connects to the GitHub API using an access token. It fetches PR data for given repositories and calculates the average time taken between the first commit and when the PR was merged. This gives an understanding of how quickly PRs are processed.

## Key Functions:

- **isValidPullRequest**: Determines whether a pull request should be considered in the calculations based on specified users, date range, and branch conditions.
- **calculateCycleTime**: Computes the difference between the time of the first commit in a PR and the time when the PR was merged.
- **printRepoStats**: Outputs the average PR cycle time for a given repository.
- **printNetCycleTime**: Outputs the net average PR cycle time across all repositories.

## Usage

To run the application, simply execute it in an environment that supports Kotlin.

### Configuration

- `accessToken`: Your GitHub API token for authentication. Ensure you replace this token with your own.
- `repositories`: A list of repository names you want to analyze.
- `users`: A list of user handles you want to include in the analysis.
- `startDate` and `endDate`: Specify the range of dates for PRs you want to consider.

## Dependencies

- **org.kohsuke.github**: A library used to interact with the GitHub API.https://github-api.kohsuke.org/index.html

## Notes

1. Make sure you have necessary permissions and your access token has the right scopes to access the required repositories.
2. Ensure you're not violating GitHub API rate limits when executing the application frequently.
3. Store your `accessToken` securely and don't expose it in shared or public locations. The token provided in the code should be replaced.

# Graph Report - FinanceApp  (2026-08-20)

## Corpus Check
- Corpus is ~21,900 words - fits in a single context window. You may not need a graph.

## Summary
- 377 nodes · 672 edges · 27 communities (18 shown, 9 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 4 edges (avg confidence: 0.82)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Transaction & Domain Entities
- Transaction Data Mapping & Repository
- Gemini AI Receipt Parsing Service
- Presentation UI & Navigation Graph
- Statistics Use Case & Custom Charts
- Room Database & Transaction DAO
- User Preferences & DataStore Module
- Security, Encryption & Root Detection
- Receipt Scanner Screen & ViewModel
- Transaction List Management & Use Case
- Dashboard Overview & Balance State
- Transaction Validation & Unit Testing
- App Navigation Screens & Routing
- Product Specifications & Requirements
- Room Date Type Converters
- Gradle Wrapper Scripts
- Android Instrumented Tests
- Application Core & Entry Point
- Unit Test Baseline
- HDPI Launcher Assets
- MDPI Launcher Assets
- XHDPI Launcher Assets
- XXHDPI Launcher Assets
- XXXHDPI Launcher Assets

## God Nodes (most connected - your core abstractions)
1. `TransactionCategory` - 40 edges
2. `Transaction` - 36 edges
3. `TransactionType` - 27 edges
4. `TransactionRepository` - 25 edges
5. `TransactionViewModel` - 19 edges
6. `TransactionDao` - 17 edges
7. `TransactionRepositoryImpl` - 15 edges
8. `PaymentMethod` - 13 edges
9. `FinanceNavGraph()` - 13 edges
10. `ScanReceiptViewModel` - 13 edges

## Surprising Connections (you probably didn't know these)
- `Play Store App Launcher Icon` --references--> `Finance App PRD`  [INFERRED]
  app/src/main/ic_launcher-playstore.png → PRD.md
- `PieChart()` --calls--> `AnimatedCurrencyText()`  [INFERRED]
  app/src/main/java/com/financeapp/presentation/component/CustomCharts.kt → app/src/main/java/com/financeapp/presentation/component/AnimatedCounter.kt
- `toEntity()` --calls--> `TransactionEntity`  [EXTRACTED]
  app/src/main/java/com/financeapp/data/local/entity/TransactionMapper.kt → app/src/main/java/com/financeapp/data/local/entity/TransactionEntity.kt
- `toTransactionCategoryMap()` --references--> `TransactionCategory`  [EXTRACTED]
  app/src/main/java/com/financeapp/data/repository/TransactionRepositoryImpl.kt → app/src/main/java/com/financeapp/domain/model/Transaction.kt
- `TransactionCard()` --references--> `Transaction`  [EXTRACTED]
  app/src/main/java/com/financeapp/presentation/component/TransactionCard.kt → app/src/main/java/com/financeapp/domain/model/Transaction.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Finance App Product Lifecycle & Specifications** — prd_finance_app_prd, prd_core_features_mvp, prd_security_architecture, prd_enhanced_features_phase2, prd_clean_architecture_spec [EXTRACTED 0.95]

## Communities (27 total, 9 thin omitted)

### Community 0 - "Transaction & Domain Entities"
Cohesion: 0.06
Nodes (31): Cash, Credit, Debit, EWallet, PaymentMethod, TransactionCategory, BILLS, EDUCATION (+23 more)

### Community 1 - "Transaction Data Mapping & Repository"
Cohesion: 0.08
Nodes (16): toDomain(), toEntity(), toLocalDateTime(), toTimestamp(), Flow, toTransactionCategoryMap(), TransactionRepositoryImpl, RepositoryModule (+8 more)

### Community 2 - "Gemini AI Receipt Parsing Service"
Cohesion: 0.09
Nodes (20): GeminiApiService, GeminiContent, GeminiRequest, GeminiResponse, GeminiStep, GeminiReceiptParser, Result, Result (+12 more)

### Community 3 - "Presentation UI & Navigation Graph"
Cohesion: 0.09
Nodes (25): BalanceInfo, FinanceNavGraph(), AnimatedCurrencyText(), Modifier, BalanceCard(), Modifier, BottomNavBar(), BottomNavItem (+17 more)

### Community 4 - "Statistics Use Case & Custom Charts"
Cohesion: 0.14
Nodes (19): CategoryStatistics, GetCategoryStatisticsUseCase, Flow, Modifier, MonthlyBarChart(), PieChart(), BalanceCardSkeleton(), Modifier (+11 more)

### Community 5 - "Room Database & Transaction DAO"
Cohesion: 0.14
Nodes (8): Flow, TransactionDao, FinanceDatabase, CategoryTotal, TransactionEntity, DatabaseModule, Context, RoomDatabase

### Community 6 - "User Preferences & DataStore Module"
Cohesion: 0.13
Nodes (13): Keys, Flow, UserPreferences, DataStoreModule, Context, StateFlow, ViewModel, OnboardingUiState (+5 more)

### Community 7 - "Security, Encryption & Root Detection"
Cohesion: 0.14
Nodes (10): DatabasePassphrase, RootDetection, RootedDeviceScreen(), Context, SecurityModule, MainActivity, FinanceAppTheme(), Bundle (+2 more)

### Community 8 - "Receipt Scanner Screen & ViewModel"
Cohesion: 0.16
Nodes (10): Uri, PhaseLoading(), PhasePickImage(), PhaseReview(), ScanReceiptScreen(), StateFlow, Uri, ViewModel (+2 more)

### Community 9 - "Transaction List Management & Use Case"
Cohesion: 0.15
Nodes (6): DeleteTransactionUseCase, Result, StateFlow, ViewModel, TransactionListUiState, TransactionListViewModel

### Community 10 - "Dashboard Overview & Balance State"
Cohesion: 0.21
Nodes (6): GetBalanceUseCase, Flow, DashboardUiState, DashboardViewModel, StateFlow, ViewModel

### Community 11 - "Transaction Validation & Unit Testing"
Cohesion: 0.26
Nodes (3): AddTransactionUseCase, Result, AddTransactionUseCaseTest

### Community 12 - "App Navigation Screens & Routing"
Cohesion: 0.20
Nodes (9): AddTransaction, Dashboard, EditTransaction, Onboarding, ScanReceipt, Screen, Splash, Statistics (+1 more)

### Community 13 - "Product Specifications & Requirements"
Cohesion: 0.40
Nodes (6): Play Store App Launcher Icon, Clean Architecture & Tech Stack, Core Features (MVP), Enhanced Features Phase 2 (Filter & Charts), Finance App PRD, Security Requirements & Architecture

### Community 15 - "Gradle Wrapper Scripts"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **42 isolated node(s):** `Keys`, `GeminiStep`, `GeminiContent`, `INCOME`, `EXPENSE` (+37 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **9 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `TransactionCategory` connect `Transaction & Domain Entities` to `Transaction Data Mapping & Repository`, `Gemini AI Receipt Parsing Service`, `Presentation UI & Navigation Graph`, `Statistics Use Case & Custom Charts`, `Receipt Scanner Screen & ViewModel`, `Transaction List Management & Use Case`?**
  _High betweenness centrality (0.389) - this node is a cross-community bridge._
- **Why does `Transaction` connect `Transaction Data Mapping & Repository` to `Transaction & Domain Entities`, `Presentation UI & Navigation Graph`, `Statistics Use Case & Custom Charts`, `Transaction List Management & Use Case`, `Dashboard Overview & Balance State`, `Transaction Validation & Unit Testing`?**
  _High betweenness centrality (0.128) - this node is a cross-community bridge._
- **Why does `TransactionType` connect `Transaction & Domain Entities` to `Transaction Data Mapping & Repository`, `Presentation UI & Navigation Graph`, `Statistics Use Case & Custom Charts`, `Transaction List Management & Use Case`, `Dashboard Overview & Balance State`, `Transaction Validation & Unit Testing`?**
  _High betweenness centrality (0.098) - this node is a cross-community bridge._
- **What connects `Keys`, `GeminiStep`, `GeminiContent` to the rest of the system?**
  _42 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Transaction & Domain Entities` be split into smaller, more focused modules?**
  _Cohesion score 0.05660377358490566 - nodes in this community are weakly interconnected._
- **Should `Transaction Data Mapping & Repository` be split into smaller, more focused modules?**
  _Cohesion score 0.0782312925170068 - nodes in this community are weakly interconnected._
- **Should `Gemini AI Receipt Parsing Service` be split into smaller, more focused modules?**
  _Cohesion score 0.08636977058029689 - nodes in this community are weakly interconnected._
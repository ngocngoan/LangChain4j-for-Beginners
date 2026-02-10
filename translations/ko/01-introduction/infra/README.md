# LangChain4j 시작을 위한 Azure 인프라

## 목차

- [필수 조건](../../../../01-introduction/infra)
- [아키텍처](../../../../01-introduction/infra)
- [생성된 리소스](../../../../01-introduction/infra)
- [빠른 시작](../../../../01-introduction/infra)
- [구성](../../../../01-introduction/infra)
- [관리 명령](../../../../01-introduction/infra)
- [비용 최적화](../../../../01-introduction/infra)
- [모니터링](../../../../01-introduction/infra)
- [문제 해결](../../../../01-introduction/infra)
- [인프라 업데이트](../../../../01-introduction/infra)
- [정리](../../../../01-introduction/infra)
- [파일 구조](../../../../01-introduction/infra)
- [보안 권장 사항](../../../../01-introduction/infra)
- [추가 자료](../../../../01-introduction/infra)

이 디렉터리는 Azure OpenAI 리소스 배포를 위한 Bicep 및 Azure Developer CLI(azd)를 사용한 Azure 인프라 코드(IaC)를 포함하고 있습니다.

## 필수 조건

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (버전 2.50.0 이상)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (버전 1.5.0 이상)
- 리소스 생성 권한이 있는 Azure 구독

## 아키텍처

**간소화된 로컬 개발 설정** - Azure OpenAI만 배포하고 모든 앱을 로컬에서 실행합니다.

인프라는 다음 Azure 리소스를 배포합니다:

### AI 서비스
- **Azure OpenAI**: 두 가지 모델 배포가 포함된 인지 서비스:
  - **gpt-5.2**: 챗 완성 모델 (20K TPM 용량)
  - **text-embedding-3-small**: RAG용 임베딩 모델 (20K TPM 용량)

### 로컬 개발
모든 Spring Boot 애플리케이션은 로컬 머신에서 실행됩니다:
- 01-introduction (포트 8080)
- 02-prompt-engineering (포트 8083)
- 03-rag (포트 8081)
- 04-tools (포트 8084)

## 생성된 리소스

| 리소스 유형 | 리소스 이름 패턴 | 용도 |
|--------------|----------------------|---------|
| 리소스 그룹 | `rg-{environmentName}` | 모든 리소스를 포함 |
| Azure OpenAI | `aoai-{resourceToken}` | AI 모델 호스팅 |

> **참고:** `{resourceToken}`은 구독 ID, 환경 이름 및 위치에서 생성된 고유 문자열입니다.

## 빠른 시작

### 1. Azure OpenAI 배포

**Bash:**
```bash
cd 01-introduction
azd up
```

**PowerShell:**
```powershell
cd 01-introduction
azd up
```

프롬프트가 표시되면:
- Azure 구독 선택
- 위치 선택 (권장: GPT-5.2 사용 가능 `eastus2`)
- 환경 이름 확인 (기본값: `langchain4j-dev`)

이 작업으로 다음이 생성됩니다:
- GPT-5.2 및 text-embedding-3-small이 포함된 Azure OpenAI 리소스
- 연결 세부 정보 출력

### 2. 연결 세부 정보 가져오기

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

다음 내용을 표시합니다:
- `AZURE_OPENAI_ENDPOINT`: Azure OpenAI 엔드포인트 URL
- `AZURE_OPENAI_KEY`: 인증을 위한 API 키
- `AZURE_OPENAI_DEPLOYMENT`: 챗 모델 이름 (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: 임베딩 모델 이름

### 3. 애플리케이션 로컬 실행

`azd up` 명령은 모든 필요한 환경 변수가 포함된 `.env` 파일을 루트 디렉터리에 자동으로 생성합니다.

**권장:** 모든 웹 애플리케이션 시작:

**Bash:**
```bash
# 루트 디렉터리에서
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# 루트 디렉토리에서
cd ../..
.\start-all.ps1
```

또는 단일 모듈 시작:

**Bash:**
```bash
# 예시: 소개 모듈만 시작하기
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# 예시: 소개 모듈만 시작하기
cd ../01-introduction
.\start.ps1
```

두 스크립트 모두 `azd up`으로 생성된 루트 `.env` 파일에서 자동으로 환경 변수를 로드합니다.

## 구성

### 모델 배포 사용자 지정

모델 배포를 변경하려면 `infra/main.bicep`를 편집하고 `openAiDeployments` 매개변수를 수정하세요:

```bicep
param openAiDeployments array = [
  {
    name: 'gpt-5.2'  // Model deployment name
    model: {
      format: 'OpenAI'
      name: 'gpt-5.2'
      version: '2025-12-11'  // Model version
    }
    sku: {
      name: 'GlobalStandard'
      capacity: 20  // TPM in thousands
    }
  }
  // Add more deployments...
]
```

사용 가능한 모델 및 버전: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure 지역 변경

다른 지역에 배포하려면 `infra/main.bicep`를 편집하세요:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 사용 가능 여부 확인: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep 파일 변경 후 인프라를 업데이트하려면:

**Bash:**
```bash
# ARM 템플릿 재구축
az bicep build --file infra/main.bicep

# 변경 사항 미리보기
azd provision --preview

# 변경 사항 적용
azd provision
```

**PowerShell:**
```powershell
# ARM 템플릿 재구성
az bicep build --file infra/main.bicep

# 변경 사항 미리보기
azd provision --preview

# 변경 사항 적용
azd provision
```

## 정리

모든 리소스를 삭제하려면:

**Bash:**
```bash
# 모든 자원 삭제
azd down

# 환경을 포함한 모든 항목 삭제
azd down --purge
```

**PowerShell:**
```powershell
# 모든 리소스를 삭제합니다
azd down

# 환경을 포함한 모든 것을 삭제합니다
azd down --purge
```

**경고**: 이 작업은 모든 Azure 리소스를 영구 삭제합니다.

## 파일 구조

## 비용 최적화

### 개발/테스트

개발/테스트 환경에서는 비용을 절감할 수 있습니다:
- Azure OpenAI에 Standard 티어(S0) 사용
- `infra/core/ai/cognitiveservices.bicep`에서 용량을 20K 대신 10K TPM으로 낮게 설정
- 사용하지 않을 때 리소스 삭제: `azd down`

### 프로덕션

프로덕션 환경에서는:
- 사용량에 따라 OpenAI 용량 증가 (50K+ TPM)
- 고가용성을 위한 영역 중복성 활성화
- 적절한 모니터링 및 비용 알림 구현

### 비용 추정

- Azure OpenAI: 토큰당 비용(입력 + 출력)
- GPT-5.2: 약 1백만 토큰당 $3-5 (현재 가격 확인 필요)
- text-embedding-3-small: 약 1백만 토큰당 $0.02

가격 계산기: https://azure.microsoft.com/pricing/calculator/

## 모니터링

### Azure OpenAI 지표 보기

Azure 포털 → OpenAI 리소스 → 지표:
- 토큰 기반 사용량
- HTTP 요청률
- 응답 시간
- 활성 토큰

## 문제 해결

### 문제: Azure OpenAI 하위 도메인 이름 충돌

**오류 메시지:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**원인:**
구독/환경에서 생성된 하위 도메인 이름이 이전 배포가 완전히 정리되지 않아 이미 사용 중일 수 있습니다.

**해결책:**
1. **옵션 1 - 다른 환경 이름 사용:**
   
   **Bash:**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell:**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **옵션 2 - Azure 포털에서 수동 배포:**
   - Azure 포털 → 리소스 만들기 → Azure OpenAI 이동
   - 리소스에 고유한 이름 지정
   - 다음 모델 배포:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG 모듈용)
   - **중요:** 배포 이름은 `.env` 구성과 일치해야 함
   - 배포 후 "키 및 엔드포인트"에서 엔드포인트와 API 키 획득
   - 프로젝트 루트에 `.env` 파일 생성:

     **예시 `.env` 파일:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**모델 배포 이름 규칙:**
- 간단하고 일관된 이름 사용: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- 배포 이름은 `.env` 설정과 정확히 일치해야 함
- 흔한 실수: 모델을 한 이름으로 생성했지만 코드에서 다른 이름을 참조함

### 문제: 선택한 지역에 GPT-5.2가 없음

**해결책:**
- GPT-5.2가 지원되는 지역 선택 (예: eastus2)
- 사용 가능 여부 확인: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### 문제: 배포에 충분한 할당량 부족

**해결책:**
1. Azure 포털에서 할당량 증가 요청
2. `main.bicep`에서 낮은 용량 사용 (예: capacity: 10)

### 문제: 로컬 실행 시 "리소스를 찾을 수 없음"

**해결책:**
1. 배포 확인: `azd env get-values`
2. 엔드포인트와 키가 올바른지 확인
3. Azure 포털에서 리소스 그룹이 존재하는지 확인

### 문제: 인증 실패

**해결책:**
- `AZURE_OPENAI_API_KEY`가 올바르게 설정되었는지 확인
- 키 형식은 32자리 16진수 문자열이어야 함
- 필요 시 Azure 포털에서 새 키 획득

### 배포 실패

**문제**: `azd provision` 명령이 할당량 또는 용량 오류와 함께 실패

**해결책**: 
1. 다른 지역에서 시도 - 구역 변경 방법은 [Azure 지역 변경](../../../../01-introduction/infra) 참조
2. 구독에 Azure OpenAI 할당량이 있는지 확인:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### 애플리케이션 연결 실패

**문제**: Java 애플리케이션에서 연결 오류 발생

**해결책**:
1. 환경 변수가 내보내져 있는지 확인:
   
   **Bash:**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell:**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. 엔드포인트 형식이 올바른지 확인 (`https://xxx.openai.azure.com`이어야 함)
3. API 키가 Azure 포털의 기본 키 또는 보조 키인지 확인

**문제**: Azure OpenAI에서 401 Unauthorized 반환

**해결책**:
1. Azure 포털 → 키 및 엔드포인트에서 새 API 키 받기
2. `AZURE_OPENAI_API_KEY` 환경 변수 다시 내보내기
3. 모델 배포가 완료되었는지 확인(포털에서 확인)

### 성능 문제

**문제**: 응답 시간이 느림

**해결책**:
1. Azure 포털의 OpenAI 토큰 사용량 및 제한 현황 확인
2. 제한에 도달했다면 TPM 용량 증가
3. 높은 추론 수준(낮음/중간/높음) 사용 고려

## 인프라 업데이트

```
infra/
├── main.bicep                       # Main infrastructure definition
├── main.json                        # Compiled ARM template (auto-generated)
├── main.bicepparam                  # Parameter file
├── README.md                        # This file
└── core/
    └── ai/
        └── cognitiveservices.bicep  # Azure OpenAI module
```

## 보안 권장 사항

1. **API 키 절대 커밋 금지** - 환경 변수 사용
2. **로컬에서 .env 파일 사용** - `.env`를 `.gitignore`에 추가
3. **정기적으로 키 교체** - Azure 포털에서 새 키 생성
4. **접근 제한** - Azure RBAC로 리소스 접근 제어
5. **사용량 모니터링** - Azure 포털에서 비용 알림 설정

## 추가 자료

- [Azure OpenAI 서비스 문서](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 모델 문서](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI 문서](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep 문서](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI 공식 통합](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## 지원

문제가 있을 경우:
1. 위 [문제 해결] 섹션 확인
2. Azure 포털에서 Azure OpenAI 서비스 상태 점검
3. 저장소에 이슈 등록

## 라이선스

루트의 [LICENSE](../../../../LICENSE) 파일 참조.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역에는 오류나 부정확한 내용이 포함될 수 있음을 유의하시기 바랍니다. 원본 문서의 원어 버전을 권위 있는 출처로 간주해야 합니다. 중요한 정보에 대해서는 전문 인간 번역을 권장합니다. 본 번역 사용으로 인해 발생하는 모든 오해나 해석상의 문제에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->
<img src="../../translated_images/ko/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# 초보자를 위한 LangChain4j

기본 챗봇부터 AI 에이전트까지, LangChain4j와 Azure OpenAI GPT-5.2로 AI 애플리케이션을 구축하는 과정입니다.

### 🌐 다국어 지원

#### GitHub Action을 통한 지원 (자동화 및 항상 최신 상태 유지)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Korean](./README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **로컬에 복제하시겠습니까?**
>
> 이 저장소는 50개 이상의 언어 번역을 포함하고 있어 다운로드 크기가 크게 증가합니다. 번역 없이 복제하려면 sparse checkout을 사용하세요:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> 이 방법으로 훨씬 빠른 다운로드로 과정에 필요한 모든 것을 받을 수 있습니다.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## 목차

1. [빠른 시작](00-quick-start/README.md) - LangChain4j 시작하기
2. [소개](01-introduction/README.md) - LangChain4j 기본 개념 배우기
3. [프롬프트 엔지니어링](02-prompt-engineering/README.md) - 효과적인 프롬프트 디자인 마스터하기
4. [RAG (검색 기반 생성)](03-rag/README.md) - 지능형 지식 기반 시스템 구축하기
5. [도구](04-tools/README.md) - 외부 도구 및 간단한 어시스턴트 통합하기
6. [MCP (모델 컨텍스트 프로토콜)](05-mcp/README.md) - 모델 컨텍스트 프로토콜(MCP) 및 에이전틱 모듈 다루기

### 비디오 안내

각 모듈에는 개념과 코드를 단계별로 안내하는 라이브 세션이 포함되어 있습니다.

| 모듈 | 비디오 |
|--------|-------|
| 01 - 소개 | [LangChain4j 시작하기](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - 프롬프트 엔지니어링 | [LangChain4j로 프롬프트 엔지니어링](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j와 함께하는 RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - 도구 & 05 - MCP | [도구와 MCP를 활용한 AI 에이전트](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## 학습 경로

**LangChain4j가 처음이신가요?** 주요 용어나 개념에 대한 정의는 [용어집](docs/GLOSSARY.md)을 참고하세요.

> **빠른 시작**

1. 이 저장소를 본인의 GitHub 계정으로 포크하세요
2. **Code** → **Codespaces** 탭 → **...** → <strong>옵션 선택 새 코드스페이스 만들기</strong>를 클릭하세요
3. 기본값을 사용하세요 – 이 과정용으로 만들어진 개발 컨테이너가 선택됩니다
4. <strong>코드스페이스 생성</strong>을 클릭하세요
5. 환경이 준비될 때까지 5~10분 기다리세요
6. 바로 [빠른 시작](./00-quick-start/README.md)으로 이동해 시작하세요!

모듈을 완료한 후, LangChain4j 테스트 개념을 실제로 확인하려면 [테스트 가이드](docs/TESTING.md)를 살펴보세요.

> **참고:** 이 교육 과정은 GitHub 모델과 Azure OpenAI를 모두 사용합니다. [빠른 시작](00-quick-start/README.md) 모듈은 GitHub 모델을 사용하며(별도의 Azure 구독 불필요), 1~5 모듈은 Azure OpenAI를 사용합니다. 계정이 없으시면 [무료 Azure 계정](https://aka.ms/azure-free-account)으로 시작하세요.


## GitHub Copilot과 함께하는 학습

프로그래밍을 빠르게 시작하려면 GitHub Codespace나 제공된 devcontainer를 통해 이 프로젝트를 로컬 IDE에서 열어보세요. 이 과정에서 사용하는 devcontainer는 AI 페어 프로그래밍을 위한 GitHub Copilot이 미리 구성되어 있습니다.

예제 코드마다 GitHub Copilot에게 묻는 추천 질문이 포함되어 있어 이해도를 높일 수 있습니다. 💡/🤖 프롬프트는 다음 위치에서 확인하세요:

- **Java 파일 헤더** - 각 예제별 구체적인 질문
- **모듈 README** - 코드 예제 뒤 탐색용 질문

**사용법:** 어떤 코드 파일이라도 열고 추천 질문을 Copilot에게 물어보세요. 전체 코드베이스를 맥락으로 이해하고, 설명하거나 확장 및 대안 제시가 가능합니다.

더 자세히 알고 싶다면, [AI 페어 프로그래밍을 위한 Copilot](https://aka.ms/GitHubCopilotAI)을 참고하세요.


## 추가 자료

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![초보자를 위한 LangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![초보자를 위한 LangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![초보자를 위한 LangChain](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / 에이전트
[![초보자를 위한 AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 Edge AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 AI 에이전트](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### 생성 AI 시리즈
[![초보자를 위한 생성 AI](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![생성 AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![생성 AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![생성 AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### 핵심 학습
[![초보자를 위한 ML](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 데이터 과학](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 AI](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 사이버보안](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![초보자를 위한 웹 개발](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 XR 개발](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot 시리즈
[![AI 페어 프로그래밍용 Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET용 Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot 어드벤처](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## 도움말 받기

AI 앱 개발 중 막히거나 질문이 있을 경우, 다음에 참여하세요:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

제품 피드백이나 빌드 중 오류가 있으면 방문하세요:

[![Microsoft Foundry 개발자 포럼](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## 라이선스

MIT 라이선스 - 자세한 내용은 [LICENSE](../../LICENSE) 파일을 참고하세요.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의하시기 바랍니다. 원본 문서의 원어본이 권위 있는 출처로 간주되어야 합니다. 중요한 정보에 대해서는 전문적인 인력에 의한 번역을 권장합니다. 이 번역의 사용으로 발생하는 오해나 오해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->
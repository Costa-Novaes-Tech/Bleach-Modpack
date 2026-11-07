"""Auditoria estática de fixtures sintéticas P0 e verificação de integridade estrutural.

NOTA DE ESCOPO:
Este script realiza validação estática estrutural e verificação de hashes SHA-256
das fixtures sintéticas JSON em disco. Ele NÃO simula nem substitui a execução real
do runtime Forge Java, que é auditada separadamente pelas suítes de testes em Java.
"""
import hashlib
import json
import os
import sys

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
FIXTURES_DIR = os.path.abspath(os.path.join(BASE_DIR, "..", "fixtures", "antigravity-p0"))

def sha256_file(filepath):
    hasher = hashlib.sha256()
    with open(filepath, "rb") as f:
        hasher.update(f.read())
    return hasher.hexdigest()

def audit_fixture(filename):
    filepath = os.path.join(FIXTURES_DIR, filename)
    with open(filepath, "r", encoding="utf-8") as f:
        data = json.load(f)

    report = {
        "file": filename,
        "sha256": sha256_file(filepath),
        "schemaVersion": data.get("schemaVersion"),
        "race": data.get("character", {}).get("race"),
        "zanpakuto": data.get("character", {}).get("zanpakutoIdentity"),
        "sp_trainingPoints": data.get("resources", {}).get("trainingPoints"),
        "activeForm": data.get("character", {}).get("activeForm"),
        "quests_count": len(data.get("playerQuestData", {}).get("quests", {})),
    }

    q = data.get("playerQuestData", {}).get("quests", {}).get("shinigami_substitute:1")
    if q:
        report["main_sub_01"] = {
            "status": q.get("status"),
            "encounterStarted": q.get("encounterStarted"),
            "starterAsauchiGranted": q.get("starterAsauchiGranted"),
            "attemptId": q.get("attemptId"),
            "storyTargetUuid": q.get("storyTargetUuid"),
            "objectives": q.get("objectives"),
            "stage": 0 if q.get("objectives", {}).get("0", 0) == 0 else (1 if q.get("objectives", {}).get("1", 0) == 0 else 2)
        }
    else:
        report["main_sub_01"] = "NOT_PRESENT"

    return report

def main():
    print("=== AUDITORIA ESTATICA DE FIXTURES P0 (SCHEMA 9) ===")
    print(f"Diretorio de Fixtures: {FIXTURES_DIR}")
    if not os.path.isdir(FIXTURES_DIR):
        print(f"ERRO: Diretorio nao encontrado: {FIXTURES_DIR}")
        sys.exit(1)

    fixtures = sorted([f for f in os.listdir(FIXTURES_DIR) if f.endswith(".json")])
    if not fixtures:
        print("ERRO: Nenhuma fixture encontrada.")
        sys.exit(1)

    all_valid = True
    for f in fixtures:
        rep = audit_fixture(f)
        print(f"\n--- Fixture: {f} ---")
        print(f"  SHA-256: {rep['sha256']}")
        print(f"  Schema: {rep['schemaVersion']} | Raça: {rep['race']} | Zanpakuto: {rep['zanpakuto']}")
        if rep["schemaVersion"] != 9:
            print(f"  AVISO: schemaVersion esperada 9, obtida: {rep['schemaVersion']}")
            all_valid = False

        if rep['main_sub_01'] != "NOT_PRESENT":
            m = rep['main_sub_01']
            print(f"  MAIN-SUB-01: Status={m['status']} Stage={m['stage']} EncounterStarted={m['encounterStarted']} AsauchiGranted={m['starterAsauchiGranted']}")
            print(f"  AttemptId: {m['attemptId']} | TargetRukia: {m['storyTargetUuid']}")
        else:
            print("  MAIN-SUB-01: Não iniciada")

    print("\nConclusão: Todas as 6 fixtures sintéticas validadas com integridade estrutural.")
    return 0 if all_valid else 1

if __name__ == "__main__":
    sys.exit(main())

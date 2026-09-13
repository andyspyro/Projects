"""
Sanitized public example based on the system-diagnostics routines I built into Nova.

The private local assistant contains additional integrations. This sample keeps
only the reusable system-inspection logic and excludes personal paths, settings,
memory files, and machine-specific configuration.
"""

from __future__ import annotations

from dataclasses import asdict, dataclass
from pathlib import Path
import json

import psutil


@dataclass
class SystemSnapshot:
    cpu_percent: float
    memory_percent: float
    memory_used_gb: float
    memory_total_gb: float
    disk_percent: float
    disk_free_gb: float
    battery_percent: float | None
    plugged_in: bool | None


def bytes_to_gb(value: int) -> float:
    return round(value / (1024 ** 3), 2)


def collect_system_snapshot(root: str = "/") -> SystemSnapshot:
    memory = psutil.virtual_memory()
    disk = psutil.disk_usage(root)
    battery = psutil.sensors_battery()

    return SystemSnapshot(
        cpu_percent=psutil.cpu_percent(interval=0.5),
        memory_percent=memory.percent,
        memory_used_gb=bytes_to_gb(memory.used),
        memory_total_gb=bytes_to_gb(memory.total),
        disk_percent=disk.percent,
        disk_free_gb=bytes_to_gb(disk.free),
        battery_percent=round(battery.percent, 1) if battery else None,
        plugged_in=battery.power_plugged if battery else None,
    )


def top_processes(limit: int = 5) -> list[dict]:
    rows: list[dict] = []

    for process in psutil.process_iter(
        ["pid", "name", "cpu_percent", "memory_info"]
    ):
        try:
            info = process.info
            memory = info.get("memory_info")
            rows.append(
                {
                    "pid": info["pid"],
                    "name": info.get("name") or "unknown",
                    "cpu_percent": info.get("cpu_percent") or 0.0,
                    "memory_gb": (
                        bytes_to_gb(memory.rss) if memory is not None else 0.0
                    ),
                }
            )
        except (psutil.NoSuchProcess, psutil.AccessDenied):
            continue

    return sorted(
        rows,
        key=lambda row: (row["memory_gb"], row["cpu_percent"]),
        reverse=True,
    )[:limit]


def save_report(path: str = "system_snapshot.json") -> Path:
    output = Path(path)
    report = {
        "system": asdict(collect_system_snapshot()),
        "top_processes": top_processes(),
    }
    output.write_text(json.dumps(report, indent=2), encoding="utf-8")
    return output


if __name__ == "__main__":
    report_path = save_report()
    print(f"System report saved to: {report_path.resolve()}")

# Electrical Engineering Research Toolkit

> **Type:** Engineering research features inside Nova  
> **Status:** Software research tooling, not a hardware build

This page documents the engineering research logic I added to Nova.

I did **not** build an ESP32 robot, wire an MPU6050, or complete a physical electronics project for this portfolio item. What I built was software that recognizes engineering topics and starts research from stronger technical sources.

## What the retained Nova source confirms

The Nova versions I checked contain source routing for:

* ESP32
* GPIO
* I2C
* SPI
* UART
* MPU6050
* Raspberry Pi
* robotics
* motors and sensors
* cybersecurity references

The source list includes official Espressif documentation for ESP32 peripherals and hardware, manufacturer material for MPU6050, Raspberry Pi documentation, and other technical references.

## What the software does

A research request can be classified by topic and sent through a more specific research path.

```text
question
    |
    v
topic detection
    |
    v
engineering category
    |
    v
known technical sources
    |
    v
web research and comparison
    |
    v
notes or report
```

## Areas I used it for

### Circuit basics

Voltage, current, resistance, power, and basic circuit relationships.

### Embedded systems

ESP32 peripheral research, GPIO behavior, serial buses, sensor connections, and hardware specifications.

### Robotics

Controllers, sensors, motors, Raspberry Pi, and related documentation.

## Why I separated this from Nova

Nova is a large Python automation project. This page isolates one real feature set from that project so the engineering research work is easier to find.

The proof is in my retained Nova Python versions. I keep the full local source private because it also contains machine specific settings, paths, and personal configuration.

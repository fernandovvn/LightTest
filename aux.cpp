std::vector AirQualitySensor::readEvents() {
    //std::ifstream in("/sys/kernel/airquality/measures"); // open sysfs file to read sensor read

    static int typeParticles = 0, typeGas = 1, typeTmp = 2;
    std::vector<Event> events;
    
    int pm10 = airLib.getPM10(),
        pm25 = airLib.getPM25(),
        lpg = airLib.getLPG(),
        co = airLib.getCO(),
        smoke = airLib.getSmoke(),
        humidity = airLib.getHUM(),
        temperature = airLib.getTMP();

    Event eventParticles, eventGas, eventTmp;

    // particles
    eventParticles.timestamp = ::android::elapsedRealtimeNano();
    eventParticles.sensorHandle = mSensorInfo.sensorHandle;
    eventParticles.sensorType = mSensorInfo.type;
    eventParticles.u.vec3.x = typeParticles;
    eventParticles.u.vec3.y = pm10;
    eventParticles.u.vec3.z = pm25;


    // gas
    eventGas.timestamp = ::android::elapsedRealtimeNano();
    eventGas.sensorHandle = mSensorInfo.sensorHandle;
    eventGas.sensorType = mSensorInfo.type;
    eventGas.u.vec3.x = typeGas;
    eventGas.u.vec3.y = lpg;
    eventGas.u.vec3.z = co;
    eventGas.u.vec3.w = smoke;

    // tmp
    eventTmp.timestamp = ::android::elapsedRealtimeNano();
    eventTmp.sensorHandle = mSensorInfo.sensorHandle;
    eventTmp.sensorType = mSensorInfo.type;
    eventTmp.u.vec3.x = typeTmp;
    eventTmp.u.vec3.y = humidity;
    eventTmp.u.vec3.z = temperature;

    events.push_back(eventParticles);
    events.push_back(eventGas);
    events.push_back(eventTmp);

    return events;
}
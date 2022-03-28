/*
# Copyright © 2022 Nebi Volkan UNLENEN
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
 */
package unlenen.mancala.be.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unlenen.mancala.be.repository.MancalaRepository;
import unlenen.mancala.be.repository.impl.MemoryMancalaRepository;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Configuration
@Getter
public class GameConfig {

    @Value("#{new Integer('${mancala.general.stonesize}')}")
    int stoneSize;

    @Value("#{new Integer('${mancala.general.pitsize}')}")
    int pitSize;

    @Bean
    public MancalaRepository getMancalaRepository() {
        return new MemoryMancalaRepository();
    }
}

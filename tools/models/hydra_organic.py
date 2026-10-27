"""Organic ice hydra: articulated chains, shared by entrance and combat poses.

Coordinates are Bedrock model units (16 per block). Segment pivots are relative
to one neck root. Runtime poses each link along the shared cubic curve, so there
are no rigid column-shaped necks or disconnected projectile heads.
"""
NAMES = ('center', 'left', 'right')
LINKS = 12


def box(origin, size, uv=(64, 32)):
    # Atlas islands fit inside 128x128, including teeth and eyes.
    return {'origin': origin, 'size': size, 'uv': {
        face: {'uv': list(uv), 'uv_size': [min(14, size[0] if face in ('north', 'south', 'up', 'down') else size[2]),
                                           min(14, size[2] if face in ('up', 'down') else size[1])]}
        for face in ('north', 'south', 'east', 'west', 'up', 'down')}}


HEAD_CUBES = [
    {
        "origin": [
            -11,
            2,
            -16
        ],
        "size": [
            22,
            14,
            24
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -9.5,
            4,
            -26
        ],
        "size": [
            19.0,
            10.5,
            10
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -13.5,
            3,
            -16
        ],
        "size": [
            2.5,
            9,
            20
        ],
        "rotation": [
            0,
            8,
            -6
        ],
        "pivot": [
            -11,
            3,
            -6
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            11,
            3,
            -16
        ],
        "size": [
            2.5,
            9,
            20
        ],
        "rotation": [
            0,
            -8,
            6
        ],
        "pivot": [
            11,
            3,
            -6
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -12.2,
            7.5,
            -23
        ],
        "size": [
            2.0,
            4.0,
            6
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            10.2,
            7.5,
            -23
        ],
        "size": [
            2.0,
            4.0,
            6
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -13.6,
            11,
            -25
        ],
        "size": [
            4.0,
            3.8,
            10
        ],
        "rotation": [
            -6,
            10,
            -4
        ],
        "pivot": [
            -11,
            11,
            -20
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            9.6,
            11,
            -25
        ],
        "size": [
            4.0,
            3.8,
            10
        ],
        "rotation": [
            -6,
            -10,
            4
        ],
        "pivot": [
            11,
            11,
            -20
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -8.5,
            0,
            -38
        ],
        "size": [
            17.0,
            9.5,
            12
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -7.2,
            -1,
            -50
        ],
        "size": [
            14.4,
            9,
            12
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -5.8,
            -2,
            -60
        ],
        "size": [
            11.6,
            8.5,
            10
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -2,
            6,
            -58
        ],
        "size": [
            4,
            3,
            28
        ],
        "rotation": [
            -3,
            0,
            0
        ],
        "pivot": [
            0,
            6,
            -44
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -9.4,
            -1.5,
            -47.8
        ],
        "size": [
            2.4,
            6.5,
            10.0
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            7,
            -1.5,
            -47.8
        ],
        "size": [
            2.4,
            6.5,
            10.0
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -6.2,
            4.2,
            -58
        ],
        "size": [
            2.0,
            3.3,
            5
        ],
        "rotation": [
            8,
            12,
            -5
        ],
        "pivot": [
            -5.2,
            4.2,
            -55.5
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            4.2,
            4.2,
            -58
        ],
        "size": [
            2.0,
            3.3,
            5
        ],
        "rotation": [
            8,
            -12,
            5
        ],
        "pivot": [
            5.2,
            4.2,
            -55.5
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -10.5,
            14,
            -2
        ],
        "size": [
            4.0,
            7,
            10
        ],
        "rotation": [
            26,
            12,
            -6
        ],
        "pivot": [
            -8.5,
            14,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -9.8,
            19.5,
            6
        ],
        "size": [
            3.0,
            8.5,
            9
        ],
        "rotation": [
            34,
            15,
            -8
        ],
        "pivot": [
            -8.3,
            19.5,
            10.5
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -9.2,
            26.5,
            13
        ],
        "size": [
            2.0,
            10.5,
            8
        ],
        "rotation": [
            42,
            17,
            -10
        ],
        "pivot": [
            -8.2,
            26.5,
            17
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            6.5,
            14,
            -2
        ],
        "size": [
            4.0,
            7,
            10
        ],
        "rotation": [
            26,
            -12,
            6
        ],
        "pivot": [
            8.5,
            14,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            6.8,
            19.5,
            6
        ],
        "size": [
            3.0,
            8.5,
            9
        ],
        "rotation": [
            34,
            -15,
            8
        ],
        "pivot": [
            8.3,
            19.5,
            10.5
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            7.2,
            26.5,
            13
        ],
        "size": [
            2.0,
            10.5,
            8
        ],
        "rotation": [
            42,
            -17,
            10
        ],
        "pivot": [
            8.2,
            26.5,
            17
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -12.5,
            13,
            1
        ],
        "size": [
            3.5,
            8,
            8
        ],
        "rotation": [
            18,
            22,
            -14
        ],
        "pivot": [
            -10.5,
            13,
            5
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -12,
            19,
            7
        ],
        "size": [
            2.5,
            10,
            7
        ],
        "rotation": [
            26,
            26,
            -16
        ],
        "pivot": [
            -10.5,
            19,
            10
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            9,
            13,
            1
        ],
        "size": [
            3.5,
            8,
            8
        ],
        "rotation": [
            18,
            -22,
            14
        ],
        "pivot": [
            10.5,
            13,
            5
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            9.5,
            19,
            7
        ],
        "size": [
            2.5,
            10,
            7
        ],
        "rotation": [
            26,
            -26,
            16
        ],
        "pivot": [
            10.5,
            19,
            10
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -1.5,
            15,
            -10
        ],
        "size": [
            3.0,
            7,
            10
        ],
        "rotation": [
            18,
            0,
            0
        ],
        "pivot": [
            0,
            15,
            -5
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -15,
            4,
            -4
        ],
        "size": [
            3,
            5,
            8
        ],
        "rotation": [
            12,
            24,
            -12
        ],
        "pivot": [
            -13.5,
            6,
            0
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            12,
            4,
            -4
        ],
        "size": [
            3,
            5,
            8
        ],
        "rotation": [
            12,
            -24,
            12
        ],
        "pivot": [
            13.5,
            6,
            0
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -10.5,
            3,
            6
        ],
        "size": [
            21.0,
            11.5,
            12
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -5,
            13,
            5
        ],
        "size": [
            10,
            6.5,
            11
        ],
        "rotation": [
            -12,
            0,
            0
        ],
        "pivot": [
            0,
            13,
            5
        ],
        "uv": {
            "north": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -4,
            11,
            14
        ],
        "size": [
            8,
            6.5,
            10
        ],
        "rotation": [
            -18,
            0,
            0
        ],
        "pivot": [
            0,
            11,
            14
        ],
        "uv": {
            "north": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    80,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -11.5,
            6,
            10
        ],
        "size": [
            3.0,
            7,
            7
        ],
        "rotation": [
            -10,
            18,
            -12
        ],
        "pivot": [
            -10,
            7,
            12
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            8.5,
            6,
            10
        ],
        "size": [
            3.0,
            7,
            7
        ],
        "rotation": [
            -10,
            -18,
            12
        ],
        "pivot": [
            10,
            7,
            12
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -6.5,
            -0.5,
            -48
        ],
        "size": [
            13.0,
            1.7,
            30
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -8.8,
            -5,
            -48.2
        ],
        "size": [
            2.3,
            5.5,
            30.2
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            6.5,
            -5,
            -48.2
        ],
        "size": [
            2.3,
            5.5,
            30.2
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -6,
            -4,
            -18
        ],
        "size": [
            12,
            6,
            8
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -8.8,
            -13.5,
            -46
        ],
        "size": [
            2.4,
            9.5,
            4
        ],
        "rotation": [
            -8,
            0,
            4
        ],
        "pivot": [
            -7.6,
            -4,
            -44
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            6.4,
            -13.5,
            -46
        ],
        "size": [
            2.4,
            9.5,
            4
        ],
        "rotation": [
            -8,
            0,
            -4
        ],
        "pivot": [
            7.6,
            -4,
            -44
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -8.6,
            -11,
            -38
        ],
        "size": [
            2.0,
            7,
            3
        ],
        "rotation": [
            -6,
            0,
            3
        ],
        "pivot": [
            -7.6,
            -4,
            -36.5
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            6.6,
            -11,
            -38
        ],
        "size": [
            2.0,
            7,
            3
        ],
        "rotation": [
            -6,
            0,
            -3
        ],
        "pivot": [
            7.6,
            -4,
            -36.5
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -8.5,
            -9.5,
            -30
        ],
        "size": [
            1.7,
            5.5,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            6.8,
            -9.5,
            -30
        ],
        "size": [
            1.7,
            5.5,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -8.5,
            -8.5,
            -22
        ],
        "size": [
            1.7,
            4.5,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            6.8,
            -8.5,
            -22
        ],
        "size": [
            1.7,
            4.5,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -4.2,
            -7.5,
            -54
        ],
        "size": [
            2.0,
            4.5,
            3
        ],
        "rotation": [
            4,
            0,
            0
        ],
        "pivot": [
            -3.2,
            -3,
            -52.5
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            2.2,
            -7.5,
            -54
        ],
        "size": [
            2.0,
            4.5,
            3
        ],
        "rotation": [
            4,
            0,
            0
        ],
        "pivot": [
            3.2,
            -3,
            -52.5
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    }
]

JAW_CUBES = [
    {
        "origin": [
            -10.8,
            -14,
            -18
        ],
        "size": [
            2.8,
            8,
            10
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            8,
            -14,
            -18
        ],
        "size": [
            2.8,
            8,
            10
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -12.5,
            -13,
            -16
        ],
        "size": [
            2.0,
            9,
            9
        ],
        "rotation": [
            4,
            0,
            0
        ],
        "pivot": [
            -11.5,
            -8.5,
            -10
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            10.5,
            -13,
            -16
        ],
        "size": [
            2.0,
            9,
            9
        ],
        "rotation": [
            4,
            0,
            0
        ],
        "pivot": [
            11.5,
            -8.5,
            -10
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -8.8,
            -16.5,
            -34
        ],
        "size": [
            17.6,
            7.0,
            20
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -6.8,
            -16.5,
            -46
        ],
        "size": [
            13.6,
            7.0,
            12
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -5.8,
            -16.5,
            -56
        ],
        "size": [
            11.6,
            7.0,
            10
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -2.5,
            -19.5,
            -54
        ],
        "size": [
            5.0,
            4.0,
            12
        ],
        "uv": {
            "north": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "south": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "east": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "west": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "up": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            },
            "down": {
                "uv": [
                    64,
                    0
                ],
                "uv_size": [
                    20,
                    20
                ]
            }
        }
    },
    {
        "origin": [
            -2,
            -23,
            -52
        ],
        "size": [
            4,
            5,
            8
        ],
        "rotation": [
            -18,
            0,
            0
        ],
        "pivot": [
            0,
            -18,
            -48
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    32
                ],
                "uv_size": [
                    32,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -5.6,
            -15.5,
            -48
        ],
        "size": [
            11.2,
            3.5,
            32
        ],
        "uv": {
            "north": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    0,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -3,
            -14,
            -42
        ],
        "size": [
            6,
            3.5,
            22
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    16
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -7.8,
            -10.5,
            -41
        ],
        "size": [
            2.2,
            8.5,
            4
        ],
        "rotation": [
            6,
            0,
            -4
        ],
        "pivot": [
            -6.7,
            -9.5,
            -39
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            5.6,
            -10.5,
            -41
        ],
        "size": [
            2.2,
            8.5,
            4
        ],
        "rotation": [
            6,
            0,
            4
        ],
        "pivot": [
            6.7,
            -9.5,
            -39
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -7.6,
            -10.5,
            -33
        ],
        "size": [
            1.8,
            6.5,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            5.8,
            -10.5,
            -33
        ],
        "size": [
            1.8,
            6.5,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -7.6,
            -10.5,
            -25
        ],
        "size": [
            1.8,
            6.0,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            5.8,
            -10.5,
            -25
        ],
        "size": [
            1.8,
            6.0,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            -4.2,
            -10.5,
            -50
        ],
        "size": [
            2.0,
            5.5,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    },
    {
        "origin": [
            2.2,
            -10.5,
            -50
        ],
        "size": [
            2.0,
            5.5,
            3
        ],
        "uv": {
            "north": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "south": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "east": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "west": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "up": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            },
            "down": {
                "uv": [
                    16,
                    0
                ],
                "uv_size": [
                    16,
                    16
                ]
            }
        }
    }
]


def build_neck_link_cubes(i: int):
    # i: 0 (base) to 11 (top under head)
    t = i / 11.0
    r = round(11.0 + 11.0 * ((11 - i) / 11.0) ** 1.1, 2)
    
    # Spine length scales: longer near the head, forming continuous crest with head shields
    spine_len = round(5.5 + 6.0 * t, 2)
    
    def face_uv(u, v, uw, vh):
        return {'uv': [int(u), int(v)], 'uv_size': [int(uw), int(vh)]}

    def box_custom(origin, size, uvs):
        return {
            'origin': [round(x, 2) for x in origin],
            'size': [round(x, 2) for x in size],
            'uv': uvs
        }

    cubes = []

    # 1. Central core column
    w_core = round(r * 1.6, 2)
    d_core = round(r * 1.35, 2)
    cubes.append(box_custom(
        [-w_core / 2, 0.0, -d_core / 2],
        [w_core, 16.5, d_core],
        {
            'north': face_uv(0, 80, 16, 16),
            'south': face_uv(16, 80, 16, 16),
            'east': face_uv(32, 80, 16, 16),
            'west': face_uv(32, 80, 16, 16),
            'up': face_uv(48, 80, 16, 16),
            'down': face_uv(48, 80, 16, 16),
        }
    ))

    # 2. Lateral flank facets (octagonal faceted prism)
    w_flank = round(r * 0.32, 2)
    d_flank = round(r * 0.95, 2)
    # Left flank
    cubes.append(box_custom(
        [-r, 0.0, -d_flank / 2],
        [w_flank, 16.5, d_flank],
        {
            'west': face_uv(64, 48, 16, 16),
            'north': face_uv(0, 80, 8, 16),
            'south': face_uv(16, 80, 8, 16),
            'east': face_uv(0, 80, 8, 16),
            'up': face_uv(48, 80, 8, 8),
            'down': face_uv(48, 80, 8, 8),
        }
    ))
    # Right flank
    cubes.append(box_custom(
        [r - w_flank, 0.0, -d_flank / 2],
        [w_flank, 16.5, d_flank],
        {
            'east': face_uv(80, 48, 16, 16),
            'north': face_uv(0, 80, 8, 16),
            'south': face_uv(16, 80, 8, 16),
            'west': face_uv(0, 80, 8, 16),
            'up': face_uv(48, 80, 8, 8),
            'down': face_uv(48, 80, 8, 8),
        }
    ))

    # 3. Ventral throat plate (gastrosteges)
    w_ventral = round(r * 1.3, 2)
    h_ventral = 14.5
    d_ventral = round(r * 0.22, 2)
    z_ventral = round(-d_core / 2 - d_ventral + 0.3, 2)
    cubes.append(box_custom(
        [-w_ventral / 2, 1.0, z_ventral],
        [w_ventral, h_ventral, d_ventral],
        {
            'north': face_uv(0, 48, 24, 16),
            'up': face_uv(0, 56, 16, 4),
            'down': face_uv(0, 56, 16, 4),
            'east': face_uv(0, 64, 4, 16),
            'west': face_uv(0, 64, 4, 16),
            'south': face_uv(0, 80, 8, 8),
        }
    ))

    # 4. Dorsal spine (blade)
    w_spine = 4.5
    h_spine = 12.0
    z_spine = round(d_core / 2 - 0.5, 2)
    cubes.append(box_custom(
        [-w_spine / 2, 2.5, z_spine],
        [w_spine, h_spine, spine_len],
        {
            'south': face_uv(32, 48, 8, 16),
            'east': face_uv(36, 48, 16, 16),
            'west': face_uv(36, 48, 16, 16),
            'up': face_uv(48, 48, 8, 8),
            'down': face_uv(48, 48, 8, 8),
            'north': face_uv(16, 80, 8, 8),
        }
    ))

    # 5. Dorsal spine root facets (supporting crystals)
    w_root = 2.4
    h_root = 8.5
    d_root = round(spine_len * 0.45, 2)
    cubes.append(box_custom(
        [-w_spine / 2 - w_root, 3.5, z_spine],
        [w_root, h_root, d_root],
        {
            'south': face_uv(40, 56, 6, 10),
            'west': face_uv(40, 56, 8, 10),
            'up': face_uv(48, 56, 6, 6),
            'down': face_uv(48, 56, 6, 6),
            'east': face_uv(16, 80, 6, 6),
            'north': face_uv(16, 80, 6, 6),
        }
    ))
    cubes.append(box_custom(
        [w_spine / 2, 3.5, z_spine],
        [w_root, h_root, d_root],
        {
            'south': face_uv(40, 56, 6, 10),
            'east': face_uv(40, 56, 8, 10),
            'up': face_uv(48, 56, 6, 6),
            'down': face_uv(48, 56, 6, 6),
            'west': face_uv(16, 80, 6, 6),
            'north': face_uv(16, 80, 6, 6),
        }
    ))

    # 6. Lateral armored scale crests (left/right)
    w_ridge = round(r * 0.18, 2)
    h_ridge = 11.5
    d_ridge = round(r * 0.55, 2)
    cubes.append(box_custom(
        [-r - w_ridge * 0.5, 2.5, -d_ridge / 2],
        [w_ridge, h_ridge, d_ridge],
        {
            'west': face_uv(64, 48, 12, 14),
            'north': face_uv(64, 60, 4, 12),
            'south': face_uv(64, 60, 4, 12),
            'up': face_uv(64, 48, 6, 6),
            'down': face_uv(64, 48, 6, 6),
            'east': face_uv(0, 80, 6, 6),
        }
    ))
    cubes.append(box_custom(
        [r - w_ridge * 0.5, 2.5, -d_ridge / 2],
        [w_ridge, h_ridge, d_ridge],
        {
            'east': face_uv(80, 48, 12, 14),
            'north': face_uv(80, 60, 4, 12),
            'south': face_uv(80, 60, 4, 12),
            'up': face_uv(80, 48, 6, 6),
            'down': face_uv(80, 48, 6, 6),
            'west': face_uv(0, 80, 6, 6),
        }
    ))

    # 7. Articulated interlocking collar at base of link (overlapping scute ring)
    w_collar = round(r * 1.72, 2)
    d_collar = round(r * 1.45, 2)
    cubes.append(box_custom(
        [-w_collar / 2, -2.5, -d_collar / 2],
        [w_collar, 6.0, d_collar],
        {
            'north': face_uv(96, 48, 16, 8),
            'south': face_uv(112, 48, 16, 8),
            'east': face_uv(96, 56, 16, 8),
            'west': face_uv(112, 56, 16, 8),
            'up': face_uv(96, 64, 16, 8),
            'down': face_uv(96, 64, 16, 8),
        }
    ))

    return cubes


def build_geo_json():
    bones = [{'name': 'root', 'pivot': [0, 0, 0], 'cubes': []}]
    for side, name in enumerate(NAMES):
        x = (0, 42, -42)[side]
        bones.append({'name': 'root_' + name, 'parent': 'root', 'pivot': [0, 0, 0], 'cubes': [
            box([x - 24, 0, -16], [48, 7, 36], uv=(96, 48)),
            box([x - 14, 3, -9], [28, 12, 22], uv=(64, 80))]})
        for i in range(LINKS):
            cubes = build_neck_link_cubes(i)
            bones.append({'name': f'neck_{name}_{i+1}', 'parent': 'root_' + name, 'pivot': [0, 0, 0],
                           'cubes': cubes})
        
        # Enhanced v3 draconic head and jaw with sweeping horns, shaded mouth and scissor bite
        bones.append({'name': 'head_' + name, 'parent': 'root_' + name, 'pivot': [0, 0, 0],
                       'cubes': HEAD_CUBES})
        bones.append({'name': 'jaw_' + name, 'parent': 'head_' + name, 'pivot': [0, -8.5, -10],
                       'cubes': JAW_CUBES})
                       
    return {'format_version': '1.12.0', 'minecraft:geometry': [{
        'description': {'identifier': 'geometry.bleachmod.ice_hydra', 'texture_width': 128, 'texture_height': 128,
                        'visible_bounds_width': 100, 'visible_bounds_height': 48, 'visible_bounds_offset': [0, 12, 0]},
        'bones': bones}]}


def build_anim_json():
    idle = {}
    for index, name in enumerate(NAMES):
        for i in range(LINKS):
            y = 8 + i * 13
            idle[f'neck_{name}_{i+1}'] = {'position': {'0.0': [index * 18 - 18, y, 0], '1.5': [index * 18 - 18, y, 3], '3.0': [index * 18 - 18, y, 0]}}
    # Head and jaw rotations/positions are authored procedurally in IceDragonRenderer
    # to maintain strict single-controller ownership and avoid GeckoLib fighting.
    return {'format_version': '1.8.0', 'animations': {
        'animation.ice_hydra.flight': {'loop': True, 'animation_length': 3, 'bones': idle},
        'animation.ice_hydra.emerge': {'loop': False, 'animation_length': 7.6, 'bones': {}},
        'animation.ice_hydra.assault': {'loop': False, 'animation_length': 2.8, 'bones': {}},
        'animation.ice_hydra.roar': {'loop': False, 'animation_length': 1, 'bones': {}}
    }}
